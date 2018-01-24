package itb2.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import itb2.engine.Controller;

/**
 * Label, displaying each message for {@value #DELAY}ms.
 * If there are multiple messages in the queue, it displays
 * the total amount of messages in square brackets.
 *
 * @author Micha Strauch
 */
public class TimedMessageLabel extends JLabel {
	private static final long serialVersionUID = 8817745804485679513L;
	
	/** Duration each message is displayed. */
	private static final long DELAY = 5000;
	
	/** Object for synchronization. */
	private final Object sync;
	
	/** Queue containing all messages */
	private final Queue<Message> messageQueue;
	
	/** Whether to go to the next message, ignoring the duration left. */
	private volatile boolean next = false;
	
	/** Constructs a new label for timed messages */
	public TimedMessageLabel() {
		sync = new Object();
		messageQueue = new LinkedList<>();
		new MessageTimer().start();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1)
					next();
				else
					clear();
			}
		});
	}
	
	/** Adds the message with the given color */
	public void addMessage(String message, Color foreground) {
		synchronized(sync) {
			messageQueue.offer(new Message(message, foreground));
			sync.notify();
		}
	}
	
	/** Displays the next message. */
	public void next() {
		synchronized(sync) {
			next = true;
			sync.notify();
		}
	}
	
	/** Clears all messages. */
	public void clear() {
		synchronized (sync) {
			messageQueue.clear();
			next = true;
			sync.notify();
		}
	}
	
	/**
	 * Thread for displaying next messages after {@value #DELAY}ms.
	 *
	 * @author Micha Strauch
	 */
	private class MessageTimer extends Thread {
		
		/** Constructor */
		public MessageTimer() {
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				long nextMessage = -1;
				Message message = null;
				int count = 0;
				
				while(true) {
					
					// Fetch message count (and maybe next message)
					synchronized(sync) {
						if(next || message == null || System.currentTimeMillis() >= nextMessage) {
							next = false;
							message = messageQueue.poll();
							nextMessage = System.currentTimeMillis() + DELAY;
						}
						count = messageQueue.size() + 1;
					}
					
					// If no message to display
					if(message == null)
						SwingUtilities.invokeLater(() -> setText(" "));
					
					// Otherwise display message
					else {
						String preview = count <= 1 ? "" : String.format(" <font color='gray'>[%d]</font>", count);
						String text = "<html>" + message + preview + "</html>";
						SwingUtilities.invokeLater(() -> setText(text));
					}
					
					// Wait for updates or until the current message is over
					synchronized (sync) {
						if(message == null) {
							sync.wait();
						} else if(System.currentTimeMillis() < nextMessage) {
							sync.wait(nextMessage - System.currentTimeMillis());
						}
					}
				}
			} catch(InterruptedException e) {
				Controller.getCommunicationManager().warning("Statusbar messages disabled: " + e.getMessage());
				setText("<html><font color='red'>Disabled &rarr; Check log for messages</font></html>");
			}
		}
	}
	
	/**
	 * Single message to display
	 * 
	 * @author Micha Strauch
	 */
	private class Message {
		
		/** Message to display */
		final String text;
		
		/** Color of the message */
		final Color foreground;
		
		/** Constructor for a message to display */
		Message(String text, Color foreground) {
			this.text = text;
			this.foreground = foreground;
		}
		
		@Override
		public String toString() {
			return String.format("<font color='#%02X%02X%02X'>%s</font>",
					foreground.getRed() & 0xFF, foreground.getGreen() & 0xFF,
					foreground.getBlue() & 0xFF, text);
		}
	}
}
