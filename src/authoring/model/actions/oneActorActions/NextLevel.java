package authoring.model.actions.oneActorActions;

import authoring.model.actions.AOneActorAction;
import authoring.model.actors.ActionType;
import authoring.model.actors.Actor;
import authoring.model.tree.Parameters;
import engine.State;

public class NextLevel extends AOneActorAction {
	/**
	 * Generated serial version ID
	 */
	private static final long serialVersionUID = 4542051448186712167L;

	@SuppressWarnings("rawtypes")
	@Override
	public void run(Parameters parameters, State state, Actor a) {
		a.updateObservers(ActionType.NEXT_LEVEL);
	}
}