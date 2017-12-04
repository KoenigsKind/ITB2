package itb2.engine;

import java.awt.Point;
import java.util.List;

import itb2.image.Image;

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
	 * Previews the given image with the given message.
	 * 
	 * @param message Description
	 * @param image   Image to show
	 */
	public void preview(String message, Image image);
	
	/**
	 * Lets the user select a certain amount of pixels on the
	 * given image. If count is zero or less, the user may select
	 * any number of pixels.  
	 * 
	 * @param message             Message to display
	 * @param requiredSelections  Number of pixels to select (Optional, set to 0 otherwise)
	 * @param image               Image to select pixels from
	 * 
	 * @return Coordinates of the selected pixels (x := column; y := row)
	 */
	public List<Point> getSelections(String message, int requiredSelections, Image image);
	
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
