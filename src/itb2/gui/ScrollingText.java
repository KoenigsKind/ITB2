package itb2.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import itb2.engine.Controller;

/**
 * Displays messages scrolling once from right to left
 * 
 * @author Micha Strauch
 */
public class ScrollingText extends JPanel {
	private static final long serialVersionUID = 1648596938512699397L;
	
	/** Gap between two messages in pixel and delay between each paint in pixels */
	private static final int GAP = 25, DELAY = 15;
	
	/** Font to use */
	private static final Font FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
	
	/** List of messages to display */
	private final List<Message> messages;
	
	/** Thread for moving messages along */
	private final Updater updater;
	
	/** Constructs this text-scrolling panel */
	public ScrollingText() {
		messages = new LinkedList<>();
		
		updater = new Updater();
		updater.start();
		
		// Set size
		FontMetrics metrics = getFontMetrics(FONT);
		Dimension size = new Dimension(0, metrics.getHeight());
		setMinimumSize(size);
		setPreferredSize(size);
	}
	
	/** Adds the message with the given color */
	public void addMessage(String message, Color foreground) {
		messages.add(new Message(message.replace('\n', ' '), foreground));
		
		updater.updateAfterDelay();
	}
	
	/** Clears all messages */
	public void clear() {
		if(messages.isEmpty())
			return; // Nothing to clear
		
		messages.clear();
		updater.updateAfterDelay();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setFont(FONT);
		FontMetrics fontMetrics = g.getFontMetrics();
		List<Message> newMessages = new LinkedList<>();
		
		int x = 0;
		int y = (getHeight() + fontMetrics.getAscent() - fontMetrics.getDescent()) / 2;
		
		// Move existing messages
		Iterator<Message> iterator = messages.iterator();
		while(iterator.hasNext()) {
			Message message = iterator.next();
			
			if(message.x == Integer.MAX_VALUE) {
				newMessages.add(message);
				continue;
			}
			
			int width = fontMetrics.stringWidth(message.text);
			
			message.x--;
			if(message.x + width < 0)
				iterator.remove();
			else if(message.x + width + GAP > x)
				x = message.x + width + GAP;
		}
		x = getWidth() > x ? getWidth() : x;
		
		// Move new messages
		for(Message message : newMessages) {
			int width = fontMetrics.stringWidth(message.text);
			
			message.x = x;
			x += width + GAP; 
		}
		
		// Draw messages
		for(Message message : messages) {
			g.setColor(message.foreground);
			g.drawString(message.text, message.x, y);
		}
		
		if(!messages.isEmpty())
			updater.updateAfterDelay();
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
		
		/** Current pixel position */
		int x = Integer.MAX_VALUE;
		
		/** Constructor for a message to display */
		Message(String text, Color foreground) {
			this.text = text;
			this.foreground = foreground;
		}
		
		@Override
		public String toString() {
			return text;
		}
		
	}
	
	/**
	 * Thread updating message movement
	 * 
	 * @author KoenigsKind
	 */
	private class Updater extends Thread {
		
		/** Constructors */
		Updater() {
			setDaemon(true);
		}
		
		/** Update after delay */
		public void updateAfterDelay() {
			synchronized (this) {
				notify();
			}
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					sleep(DELAY);
					SwingUtilities.invokeLater(() -> repaint());
					synchronized(this) {
						wait();
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				Controller.getCommunicationManager().error("Exception while updating scrolling text");
			}
		}
	}
	
}
