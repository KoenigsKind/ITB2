package itb2.gui;

import java.awt.Color;
import java.util.logging.Level;

import itb2.engine.CommunicationManager;

/**
 * Message types for {@link CommunicationManager}
 * with log level and foreground color
 * 
 * @author Micha Strauch
 */
public enum MessageType {
	
	INFO(Level.INFO, Color.BLACK),
	DEBUG(Level.CONFIG, Color.BLUE),
	WARNING(Level.WARNING, new Color(255, 85, 0)),
	ERROR(Level.SEVERE, new Color(200, 0, 0));
	
// ---------------------------------------------------------- //
	
	/** Color of message type */
	public final Color foreground;
	
	/** Log level of message type */
	public final Level level;
	
	/** Cosntructor for message type */
	private MessageType(Level level, Color foreground) {
		this.level = level;
		this.foreground = foreground;
	}
	
	/** Returns the message type belonging to the given log level */
	public static MessageType fromLevel(Level level) {
		if(level.intValue() >= ERROR.level.intValue())
			return ERROR;
		if(level.intValue() >= WARNING.level.intValue())
			return WARNING;
		if(level.intValue() >= INFO.level.intValue())
			return INFO;
		return DEBUG;
	}
	
}
