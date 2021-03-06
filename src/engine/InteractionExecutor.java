package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authoring.model.actions.IAction;
import authoring.model.actors.Actor;
import authoring.model.actors.ActorType;
import authoring.model.bundles.Bundle;
import authoring.model.game.ActorDependencyInjector;
import authoring.model.level.Level;
import authoring.model.properties.Property;
import authoring.model.tree.ActionTreeNode;
import authoring.model.tree.ActorTreeNode;
import authoring.model.tree.InteractionTreeNode;
import authoring.model.tree.ParameterTreeNode;
import authoring.model.tree.TriggerTreeNode;
import authoring.model.triggers.ITriggerEvent;
import exceptions.EngineException;
import exceptions.engine.InteractionTreeException;
import player.InputManager;
import resources.keys.PropertyKey;
import resources.keys.PropertyKeyResource;

/**
 * The InteractionExecutor runs a single level for the engine.
 * Contains most of the state of the game.
 *
 */

public class InteractionExecutor {
	private static final String ACTOR_IDENTIFIER = ActorTreeNode.class.getSimpleName();
	private static final String TRIGGER_IDENTIFIER = TriggerTreeNode.class.getSimpleName();
	private static final String ACTION_IDENTIFIER = ActionTreeNode.class.getSimpleName();

	private String currentLevelIdentifier;
	
	private State currentState;
	private State nextState;
	
	private InputManager inputMap;
	private Map<String,ITriggerEvent> triggerMap;
	private Map<String,IAction> actionMap;

	private InteractionTreeNode triggerTree;
	private Map<String, NodeLambda<InteractionTreeNode,List<?>>> lambdaMap;
	private ActorDependencyInjector depInjector;

	private InteractionExecutor () {
		this.currentLevelIdentifier = null;
		this.triggerTree = new InteractionTreeNode();
		this.inputMap = new InputManager();
		this.triggerMap = new HashMap<>();
		this.actionMap = new HashMap<>();
		
		initLambdaMap();
	}

	public InteractionExecutor (Level level, InputManager inputMap, State state, ActorDependencyInjector depInjector) {
		this();
		this.inputMap = inputMap;
		if (level != null) {
			this.currentLevelIdentifier = level.getUniqueID();
			this.triggerTree = level.getRootTree();
			this.currentState = state;
			currentState.getPropertyBundle().add(new Property<String>(PropertyKeyResource.getKey(PropertyKey.LEVEL_ID_KEY), currentLevelIdentifier));
			currentState.setActorMap(level.getActorGroups());
			if (level.getProperty(PropertyKeyResource.getKey(PropertyKey.LEVEL_BACKGROUND_KEY)) != null) {
				currentState.setInstruction(c -> {
					c.updateBackground((String)level.getProperty(PropertyKeyResource.getKey(PropertyKey.LEVEL_BACKGROUND_KEY)).getValue());
				});
			}
			
			this.triggerMap = level.getTriggerMap();
			this.actionMap = level.getActionMap();

			nextState = new State(currentState);
			
			this.depInjector = depInjector;
			setObservableActors(nextState);
		}
	}
	
	private void setObservableActors(State state) {
		for (String actorType : state.getActorMap().getMap().keySet()) {
			for (Actor actor : state.getActorMap().getMap().get(actorType)) {
				if (actor.getActorType() == ActorType.GLOBAL) {
					depInjector.hookRelation(actor);
				}
			}
		}
	}
	
	/**
	 * Runs a single step of the level. Resolves all self-triggers before external triggers.
	 * @return A {@link State} that allows the executor to communicate with the engine and player controller.
	 * @throws EngineException 
	 */
	public State run () throws EngineException {
		nextState = new State(currentState);
		setObservableActors(nextState);
		try {
			runTriggers();
		} catch (Exception e) {
			e.printStackTrace();
			throw new InteractionTreeException("Error in interaction tree", null);
		}
		nextState.getActorMap().cleanUpActors();
		currentState.merge(nextState);
		//		return new EngineHeartbeat(this, (IPlayer p) -> {}); // example lambda body: { p.pause(); }
		return currentState;
	}

	private void runTriggers () {
		for (InteractionTreeNode node : triggerTree.children()) {
			lambdaMap.get(node.getIdentifier()).apply(node, Arrays.asList(node.getValue()));;
		}
	}

	/**
	 * 
	 * @return The ID of the current level as a String.
	 */
	public String getLevelID () {
		return currentLevelIdentifier;
	}

	@SuppressWarnings("unchecked")
	private <V> void initLambdaMap () {
		lambdaMap = new HashMap<String,NodeLambda<InteractionTreeNode,List<?>>>();
		lambdaMap.put(ACTOR_IDENTIFIER, (node, list) -> {
			for(InteractionTreeNode child : node.children()){
				if (child.getIdentifier().equals(ACTOR_IDENTIFIER)) {
					lambdaMap.get(child.getIdentifier()).apply(child, cloneListAndAdd((List<String>) list, child.getValue()));
				} else {
					List<List<Actor>> comboList = new ArrayList<List<Actor>>();
					generateActorCombinations((List<String>) list, comboList);
					for (List<Actor> combo : comboList) {
						lambdaMap.get(child.getIdentifier()).apply(child, combo);
					}
				}
			}
		});
		lambdaMap.put(TRIGGER_IDENTIFIER, (node, list) -> {
			ITriggerEvent triggerEvent = triggerMap.get(node.getValue());
			if (triggerEvent.condition(((ParameterTreeNode) node).getParameters(), inputMap, currentState, ((List<Actor>)list).toArray(new Actor[list.size()]))) {
				for (InteractionTreeNode child : node.children()) {
					lambdaMap.get(child.getIdentifier()).apply(child, list);
				}
			}
		});
		lambdaMap.put(ACTION_IDENTIFIER, (node, list) -> {
			IAction action = actionMap.get(node.getValue());
			Actor[] actors = ((List<Actor>) list).stream().map(a -> {
				return nextState.getActorMap().getGroup(a.getGroupName()).get(a.getUniqueID());
			}).toArray(Actor[]::new);
			
			action.run(inputMap, ((ParameterTreeNode) node).getParameters(), nextState, actors);
//			action.run(new HashMap<String, V>(), nextState, actors);
		});
	}
	private <T> List<T> cloneListAndAdd (List<T> list, T value) {
		List<T> actorList = new ArrayList<T>(list);
		actorList.add(value);
		return actorList;
	}
	
	private void generateActorCombinations(List<String> groups, List<List<Actor>> uniques){
		generateActorCombinations (groups, uniques, new ArrayList<Actor>());
	}
	
	private void generateActorCombinations (List<String> groups, List<List<Actor>> uniques, List<Actor> current) {
		int depth = current.size();
		if (depth == groups.size()) {
			uniques.add(current);
			return;
		}
		Bundle<Actor> currentGroup = currentState.getActorMap().getGroup(groups.get(depth));
		for(Actor a : currentGroup) {
			generateActorCombinations(groups, uniques, cloneListAndAdd(current, a));
		}
	}
	@FunctionalInterface
	interface NodeLambda <A, B> { 
		public void apply (A a, B b);
	}
	protected State getCurrentState() {
		return currentState;
	}
}
