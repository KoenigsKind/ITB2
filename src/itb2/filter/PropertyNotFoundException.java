package itb2.filter;

public class PropertyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -876906854673474140L;

	public PropertyNotFoundException(String type, String property) {
		super("Property of type '" + type + "' not found: " + property);
	}

}
