package authoring.model.actions.oneActorActions;

import authoring.model.actions.AActionOneActor;
import authoring.model.actors.Actor;
import authoring.model.actors.ActorGroups;
import authoring.model.properties.Property;
import authoring.model.tree.Parameters;

public class IncreaseProperty extends AActionOneActor {
	private static final int PROPERTY_INDEX = 0;
	private static final int INCREMENT_INDEX = 0;
	private static final String STRING_LIST = "string";
	private static final String DOUBLE_LIST = "double";

	@SuppressWarnings("unchecked")
	@Override
	public void run(Parameters parameters, ActorGroups actorGroup, Actor actor) {
		String propertyName = (String) parameters.getParameterList(STRING_LIST).get(PROPERTY_INDEX);
		Double increment = (Double) parameters.getParameterList(DOUBLE_LIST).get(INCREMENT_INDEX);
		Property<Double> property = (Property<Double>) actor.getProperty(propertyName);
		property.setValue(property.getValue() + increment);
		actorGroup.addActor(actor);
	}
}
