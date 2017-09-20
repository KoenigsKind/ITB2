package itb2.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * A scroll pane that can be dragged, instead of being scrolled
 * 
 * @author Micha Strauch
 */
public class DraggableScrollPane extends JScrollPane {
	private static final long serialVersionUID = 7574613713592839284L;
	
	/** Listener for mouse dragging */
	private final MouseMotionListener dragListener;
	
	/** Listener for mouse down - to initalize dragging */
	private final MouseListener clickListener;
	
	/** Previous point since last drag-update */
	private Point prev;
	
	/**
	 * Creates a draggable scroll pane displaying the given component.
	 * Dragging occurs using the left mouse button.
	 * 
	 * @param comp Component to display
	 */
	public DraggableScrollPane(Component comp) {
		this(comp, MouseEvent.BUTTON1_DOWN_MASK);
	}
	
	/**
	 * Creates a draggable scroll pane displaying the given component.
	 * Dragging occurs using the given mask.
	 * 
	 * @param comp     Component to display
	 * @param dragMask Mask of mouse buttons for dragging
	 */
	public DraggableScrollPane(Component comp, int dragMask) {
		super(comp);
		
		setWheelScrollingEnabled(false);
		
		clickListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if( (e.getModifiersEx() & dragMask) == dragMask)
					prev = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), DraggableScrollPane.this);
			}
		};
		
		dragListener = new MouseMotionListener() {
			@Override public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if( (e.getModifiersEx() & dragMask) == dragMask) {
					Point next = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), DraggableScrollPane.this);
					
					int dX = prev.x - next.x;
					int dY = prev.y - next.y;
					
					Rectangle rect = getViewport().getVisibleRect();
					rect.translate(dX, dY);
					getViewport().scrollRectToVisible(rect);
					
					prev = next;
				}
			}
		};
		
		registerEventListener(comp);
	}
	
	@Override
	public void setViewportView(Component view) {
		Component old = getViewport().getView();
		if(old != null)
			removeEventListener(old);
		
		super.setViewportView(view);
		
		registerEventListener(view);
	}
	
	/** Registers drag-listeners for the given component */
	private void registerEventListener(Component comp) {
		comp.addMouseListener(clickListener);
		comp.addMouseMotionListener(dragListener);
	}
	
	/** Unregisters drag-listeners for the given component */
	private void removeEventListener(Component comp) {
		comp.removeMouseListener(clickListener);
		comp.removeMouseMotionListener(dragListener);
	}
	
}
