package itb2.engine;

/**
 * Manager for communication output
 * 
 * @author Micha Strauch
 */
public interface CommunicationManager {
	
	/**
	 * Prints an information message. Message can be a
	 * {@link java.util.Formatter formatted String}.
	 * 
	 * @param message Message to print
	 * @param param   Optional parameters for inserting into message
	 */
	public void info(String message, Object... param);
	
	/**
	 * Prints a debug message. Message can be a
	 * {@link java.util.Formatter formatted String}.
	 * 
	 * @param message Message to print
	 * @param param   Optional parameters for inserting into message
	 */
	public void debug(String message, Object... param);
	
	/**
	 * Prints a warning message. Message can be a
	 * {@link java.util.Formatter formatted String}.
	 * 
	 * @param message Message to print
	 * @param param   Optional parameters for inserting into message
	 */
	public void warning(String message, Object... param);
	
	/**
	 * Prints an error message. Message can be a
	 * {@link java.util.Formatter formatted String}.
	 * 
	 * @param message Message to print
	 * @param param   Optional parameters for inserting into message
	 */
	public void error(String message, Object... param);
	
	/**
	 * Sets the progress indicator to the given value:
	 * <table>
	 * 	 <tr>
	 *     <td align="right">percent &lt; 0</td>
	 *     <td align="left">&rarr; Unknown progress</td>
	 *   </tr>
	 *   <tr>
	 *     <td align="center">0 &le; percent &le; 1</td>
	 *     <td align="left">&rarr; Progress in percent (0-100%)</td>
	 *   </tr>
	 *   <tr>
	 *     <td align="left">1 &lt; percent</td>
	 *     <td align="left">&rarr; Hide progress indicator</td>
	 *   </tr>
	 * </table>
	 * 
	 * @param percent Current progress (see above)
	 */
	public void inProgress(double percent);
	
	/**
	 * Sets the progress indicator and prints an information message.
	 * 
	 * @param percent Current progress (see {@link #inProgress(double) inProgress})
	 * @param message Message to print (can be a {@link java.util.Formatter formatted String})
	 * @param param   Optional parameters for inserting into message
	 */
	default public void inProgress(double percent, String message, Object... param) {
		inProgress(percent);
		info(message, param);
	}
	
}
