package authoring.model.triggers.selfconditions;

import authoring.model.triggers.selftriggers.APropertyTest;

public class PropertyLessThan extends APropertyTest {
	/**
	 * Generated serial version
	 */
	private static final long serialVersionUID = -7330673705733755062L;

	protected boolean checkCondition(Double property, Double value) {
		return Double.compare(property, value) < 0;
	}
}
