package authoring.model.triggers.externalconditions;

import authoring.model.actions.ActionTriggerHelper;
import authoring.model.actors.Actor;
import authoring.model.properties.Property;
import authoring.model.tree.Parameters;
import authoring.model.triggers.externaltriggers.AExternalTrigger;
import player.InputManager;

public class InRange extends AExternalTrigger {

	@SuppressWarnings("unchecked")
	@Override
	public boolean condition(Parameters parameters, InputManager inputManager, Actor... actors) {

		Actor actorA = actors[0];
		Actor actorB = actors[1];

		Double rangeA = ((Property<Double>) actorA.getProperties().getComponents().get("range")).getValue();
		Double sizeB = ((Property<Double>) actorB.getProperties().getComponents().get("size")).getValue();

		double distanceToActorB = ActionTriggerHelper.distance(actorA, actorB) - sizeB;

		if (Double.compare(distanceToActorB, rangeA) <= 0) {
			return true;
		}
		return false;
	}

}