package authoring.model.properties;

import authoring.model.bundles.Identifiable;

/**
 * @author Inan
 *
 * @param <T> Generic values (either String or Double. Can be states in the future)
 */
public class Property<T> implements Identifiable, IProperty<T> {

	private T myValue;
	private String identifier;
	
	public Property(String identifier, T value) {
		this.identifier = identifier;
		this.myValue = value;
	}
	
	@Override
	public T getValue() {
		return myValue;
	}
	
	@Override
	public void setValue(T value) {
		myValue = value;
	}
	
	@Override
	public String getUniqueID() {
		return identifier;
	}
	
	@Override
	public Identifiable getCopy() {
		Property<T> copy = new Property<T>(identifier, myValue);
		return copy;
	}
}
