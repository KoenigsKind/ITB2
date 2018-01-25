package itb2.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Status bar showing progress and displaying messages 
 * 
 * @author Micha Strauch
 */
public class StatusBar extends JPanel {
	private static final long serialVersionUID = 6045262255917001097L;
	
	/** Values for showing unknown progress and hiding the progress bar */
	public static final double PROGRESS_UNKNOWN = -1, PROGRESS_HIDDEN = 2;
	
	/** Progress bar */
	private final JProgressBar progressbar;
	
	/** Label displaying messages for a certain duration. */
	private final TimedMessageLabel messages;
	
	/** EditorGui to check if debug output is enabled */
	private final EditorGui gui;
	
	/** Constructs a status bar for showing progress and displaying messages */
	public StatusBar(EditorGui gui) {
		this.gui = gui;
		
		// Instantiate objects
		progressbar = new JProgressBar();
		progressbar.setVisible(false);
		
		messages = new TimedMessageLabel();
		messages.setHorizontalAlignment(SwingConstants.RIGHT);
		
		// Create constraints for layout
		GridBagConstraints constraintProgressbar = new GridBagConstraints();
		constraintProgressbar.weightx = 0;
		constraintProgressbar.insets = new Insets(5, 5, 5, 5);
		
		GridBagConstraints constraintMessage = new GridBagConstraints();
		constraintMessage.weightx = 1;
		constraintMessage.fill = GridBagConstraints.HORIZONTAL;
		constraintMessage.insets = new Insets(5, 5, 5, 5);
		
		// Set layout
		setLayout(new GridBagLayout());
		add(progressbar, constraintProgressbar);
		add(messages, constraintMessage);
		
		setBorder(new EmptyBorder(2, 2, 2, 2));
		
		// Register log handler
		Logger.getLogger("ITB2").addHandler(new StatusBarHandler());
	}
	
	/**
	 * Sets the value of the progress bar.
	 * <p>
	 * A value below 0 means unknown, a value above 1 hides the bar.
	 * A value between 0.0 and 1.0 (both inclusive) will set the percentage.
	 * 
	 * @param progress Current progress
	 */
	public void setProgress(double progress) {
		if(0 <= progress && progress <= 1) {
			progressbar.setValue((int)(progress * 100));
		}
		
		progressbar.setIndeterminate(progress < 0);
		progressbar.setStringPainted(progress >= 0);
		progressbar.setVisible(progress <= 1);
	}
	
	/**
	 * Adds the given message of the given message type
	 * 
	 * @param message Message to display
	 * @param type    Type of the message
	 */
	public void addMessage(String message, MessageType type) {
		if(type == MessageType.DEBUG)
			return; // Don't show debug messages
		
		if(message == null || message.isEmpty())
			return; // Nothing to show
		
		messages.addMessage(message, type.foreground);
	}
	
	/**
	 * Updates status bar on incoming logs
	 * 
	 * @author Micha Strauch
	 */
	private class StatusBarHandler extends Handler {
		@Override public void close() throws SecurityException {}
		@Override public void flush() {}

		@Override
		public void publish(LogRecord record) {
			MessageType type = MessageType.fromLevel(record.getLevel());
			
			SwingUtilities.invokeLater(() -> {
				if(type == MessageType.ERROR)
					JOptionPane.showMessageDialog(gui, record.getMessage(), EditorGui.TITLE + " - Error", JOptionPane.ERROR_MESSAGE);
				else
					addMessage(record.getMessage().replaceAll("<.*?>", ""), type);
			});
		}
		
	}
	
}
