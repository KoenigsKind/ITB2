package itb2.filter;

public class PropertyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -876906854673474140L;

	public PropertyNotFoundException(String name) {
		super("Property '" + name + "' not found");
	}

	public PropertyNotFoundException(String name, Class<?> type) {
		super("Property '" + name + "' of type '" + type.getSimpleName() + "' not found");
	}

}
