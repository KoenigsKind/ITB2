package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

/**
 * Window displaying log messages
 * 
 * @author Micha Strauch
 */
public class Log extends JDialog {
	private static final long serialVersionUID = 9140862072980327691L;
	
	/** Default size for window */ 
	private static final int WIDTH = 400, HEIGHT = 200;
	
	/** Logger this window keeps track of */
	private final Logger logger;
	
	/** Object for synchronizing lock */
	private final Object sync;
	
	/** Label displaying log */
	private final JLabel log;
	
	/** Format for displaying timestamp next to log */
	private final SimpleDateFormat dateFormat;
	
	/** Templates for page and messages */
	private final String pageTemplate, messageTemplate;
	
	/** List of messages with and without debug messages */
	private String messages;
	
	/** Whether this log was shown before */
	private boolean firstShown = true;
	
	/** Creates the window for displaying log messages */
	public Log(Frame parent) {
		super(parent, EditorGui.TITLE + " - Log");
		
		sync = new Object();
		dateFormat = new SimpleDateFormat("HH:mm");
		
		StringBuilder templateBuilder = new StringBuilder("<html><head><style>");
		
		for(MessageType type : MessageType.values()) {
			templateBuilder.append(String.format(".%s {color: rgb(%d, %d, %d);}",
					type.name(),
					type.foreground.getRed(),
					type.foreground.getGreen(),
					type.foreground.getBlue()));
		}
		
		templateBuilder.append("</style></head><body><table>%s</table></body></html>");
		
		pageTemplate = templateBuilder.toString();
		messageTemplate = "<tr class='%s'><td valign='top'>%s</td><td>%s</td></tr>";
		messages = "";
		
		logger = Logger.getLogger("ITB2");
		
		boolean displayDebug = logger.isLoggable(MessageType.DEBUG.level);
		JCheckBox showDebug = new JCheckBox("Show debug messages", displayDebug);
		showDebug.addActionListener(e -> {
			Level level = showDebug.isSelected() ? MessageType.DEBUG.level : MessageType.INFO.level;
			logger.setLevel(level);
		});
		
		log = new JLabel();
		log.setVerticalAlignment(JLabel.TOP);
		log.setBackground(Color.WHITE);
		log.setOpaque(true);
		log.setText(String.format(pageTemplate, messages));
		
		JScrollPane scrollLog = new JScrollPane(log);
		scrollLog.setBorder(new BevelBorder(BevelBorder.LOWERED));
		scrollLog.getVerticalScrollBar().setUnitIncrement(16);
		
		JPanel content = new JPanel(new BorderLayout());
		content.setBorder(new EmptyBorder(5, 5, 5, 5));
		content.add(showDebug, BorderLayout.NORTH);
		content.add(scrollLog, BorderLayout.CENTER);
		
		add(content);
		
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		logger.addHandler(new LogFrameHandler());
	}
	
	/** Shows/Hides the log window */
	public void toggleDisplay() {
		if(firstShown) {
			setLocationRelativeTo(getOwner());
			firstShown = false;
		}
		setVisible(!isVisible());
	}
	
	/** Updates the log with messages */
	private void update() {
		synchronized(sync) {
			log.setText(String.format(pageTemplate, messages));
		}
	}
	
	/**
	 * Handler to receive messages
	 * 
	 * @author Micha Strauch
	 */
	private class LogFrameHandler extends Handler {
		@Override public void close() throws SecurityException {}
		@Override public void flush() {}

		@Override
		public void publish(LogRecord record) {
			MessageType type = MessageType.fromLevel(record.getLevel());
			
			String time = dateFormat.format(new Date(record.getMillis()));
			String message = String.format(messageTemplate,
					type.name(),
					time,
					record.getMessage().replaceAll("\r?\n", "<br>"));
			
			synchronized(sync) {
				messages += message;
			}
			
			SwingUtilities.invokeLater(() -> update());
		}
	}

}
