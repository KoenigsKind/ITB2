package itb2.data;

import java.util.function.Function;

/**
 * Conversion-Path
 * <p>
 * For example, if no converter from A to C is registered, but
 * there is a converter A to B and one converter B to C; the
 * conversion path will be A &rarr; B &rarr; C.
 * 
 * @author Micha Strauch
 */
public class Path<T> {
	/** Previous as posterior path */
	private final Path<T> prev, post;
	/** Converter to call */
	private final Function<T, T> converter;
	/** Description to print at {@link #toString()} */
	private final String description;
	
	/**
	 * Creates a path with only this converter in it.
	 * 
	 * @param description Description to use at {@link #toString()}
	 * @param converter   Converter to call
	 */
	public Path(String description, Function<T, T> converter) {
		this(description, null, converter, null);
	}
	
	/**
	 * Creates a path, with calling prev before converter
	 * 
	 * @param description Description to use at {@link #toString()}
	 * @param prev        Path to execute before calling converter
	 * @param converter   Converter to call
	 */
	public Path(String description, Path<T> prev, Function<T, T> converter) {
		this(description, prev, converter, null);
	}
	
	/**
	 * Creates a path, with calling post after converter
	 * 
	 * @param description Description to use at {@link #toString()}
	 * @param converter   Converter to call
	 * @param post        Path to execute after calling converter
	 */
	public Path(String description, Function<T, T> converter, Path<T> post) {
		this(description, null, converter, post);
	}
	
	/**
	 * Creates a full path, with calling prev before and post after converter
	 *
	 * @param description Description to use at {@link #toString()}
	 * @param prev        Path to execute before calling converter
	 * @param converter   Converter to call
	 * @param post        Path to execute after calling converter
	 */
	public Path(String description, Path<T> prev, Function<T, T> converter, Path<T> post) {
		if(converter == null)
			throw new NullPointerException(); // Fail fast
		
		this.prev = prev;
		this.converter = converter;
		this.post = post;
		this.description = description;
	}
	
	/**
	 * Converts the given object
	 * 
	 * @param input Object to convert
	 * 
	 * @return Converted object
	 * 
	 * @throws ConversionException If conversion is not successful
	 */
	public T convert(T input) throws ConversionException {
		if(prev != null)
			input = prev.convert(input);
		
		input = converter.apply(input);
		
		if(post != null)
			input = post.convert(input);
		
		return input;
	}
	
	/**
	 * Number of converters in this path
	 * 
	 * @return Converter count
	 */
	public int length() {
		int length = 1;
		if(prev != null)
			length += prev.length();
		if(post != null)
			length += post.length();
		return length;
	}
	
	@Override
	public String toString() {
		String output = "";
		if(prev != null)
			output += prev + " ";
		output += description;
		if(post != null)
			output += " " + post;
		return "<" + output + ">";
	}
}
