package itb2.data;

/**
 * Exception while converting.
 *
 * @author Micha Strauch
 */
public class ConversionException extends RuntimeException {
	private static final long serialVersionUID = 2051990259516033821L;

	/**
	 * Creates a conversion exception with the given message.
	 * 
	 * @param message Message of this exception
	 */
	public ConversionException(String message) {
		super(message);
	}

}
