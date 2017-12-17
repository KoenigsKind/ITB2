package itb2.gui;

import java.awt.Point;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import itb2.engine.CommunicationManager;
import itb2.engine.PreviewHandler;
import itb2.image.Image;

/**
 * Implementation of the {@link CommunicationManager}
 * 
 * @author Micha Strauch
 */
public class DefaultCommunicationManager implements CommunicationManager {
	
	/** GUI this communication manager is for */
	private final EditorGui gui;
	
	/** Status bar with progress indicator */
	private final StatusBar statusbar;
	
	/** Logger to print messages to */
	private final Logger logger;
	
	/** Creates the CommunicationManager for {@link EditorGui} */
	public DefaultCommunicationManager(EditorGui gui) {
		this.gui = gui;
		statusbar = gui.getStatusBar();
		logger = Logger.getLogger("ITB2");
	}

	@Override
	public void info(String message, Object... params) {
		if(params.length > 0)
			message = String.format(message, params);
		
		logger.log(MessageType.INFO.level, message);
	}

	@Override
	public void debug(String message, Object... params) {
		if(!logger.isLoggable(MessageType.DEBUG.level))
			return;
		
		if(params.length > 0)
			message = String.format(message, params);
		
		logger.log(MessageType.DEBUG.level, message);
		System.out.println(message);
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
	public PreviewHandler preview(String message, Image image) {
		Wrapper<ImageFrame> wrapper = new Wrapper<>();
		
		SwingUtilities.invokeLater(() -> {
			synchronized(wrapper) {
				wrapper.value = new ImageFrame(gui, image, message);
				wrapper.value.setVisible(true);
			}
		});
		
		PreviewHandler handler = new PreviewHandler() {
			@Override
			public void preview(String message, Image image) {
				synchronized (wrapper) {
					if(wrapper.value != null) {
						ImageFrame frame = wrapper.value;
						SwingUtilities.invokeLater(() -> {
							frame.setImage(image);
							frame.setTitle(message);
						});
					}
				}
			}
			
			@Override
			public void close() {
				synchronized (wrapper) {
					if(wrapper.value != null) {
						ImageFrame frame = wrapper.value;
						SwingUtilities.invokeLater(() -> frame.dispose());
					}
				}
			}
		};
		
		return handler;
	}
	
	@Override
	public List<Point> getSelections(String message, int maxSelections, Image image) {
		try {
			Wrapper<List<Point>> output = new Wrapper<List<Point>>();
			
			synchronized(output) {
				SwingUtilities.invokeLater(() -> new ImageFrame(gui, image, message, maxSelections, selections -> {
					synchronized (output) {
						output.value = selections;
						output.notifyAll();
					}
				}));
				
				output.wait();
				return output.value;
			}
		} catch(Exception e) {
			throw new RuntimeException("Could not select pixels", e);
		}
	}

	@Override
	public void inProgress(double percent) {
		SwingUtilities.invokeLater(() -> statusbar.setProgress(percent));
	}
	
	/** Helper class, that wraps a value */
	private class Wrapper<T> {
		/** Wrapped value */
		public T value;
	}

}
