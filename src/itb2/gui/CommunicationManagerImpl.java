package itb2.gui;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import itb2.engine.CommunicationManager;

/**
 * Implementation of the {@link CommunicationManager}
 * 
 * @author Micha Strauch
 */
public class CommunicationManagerImpl implements CommunicationManager {
	
	/** Status bar with progress indicator */
	private final StatusBar statusbar;
	
	/** Logger to print messages to */
	private final Logger logger;
	
	/** Creates the CommunicationManager for {@link EditorGui} */
	public CommunicationManagerImpl(EditorGui gui) {
		statusbar = gui.getStatusBar();
		logger = Logger.getGlobal();
	}

	@Override
	public void info(String message, Object... params) {
		if(params.length > 0)
			message = String.format(message, params);
		
		logger.log(MessageType.INFO.level, message);
	}

	@Override
	public void debug(String message, Object... params) {
		if(params.length > 0)
			message = String.format(message, params);
		
		logger.log(MessageType.DEBUG.level, message);
	}

	@Override
	public void warning(String message, Object... params) {
		if(params.length > 0)
			message = String.format(message, params);
		
		logger.log(MessageType.WARNING.level, message);
	}

	@Override
	public void error(String message, Object... params) {
		if(params.length > 0)
			message = String.format(message, params);
		
		logger.log(MessageType.ERROR.level, message);
	}

	@Override
	public void inProgress(double percent) {
		SwingUtilities.invokeLater(() -> statusbar.setProgress(percent));
	}

}
