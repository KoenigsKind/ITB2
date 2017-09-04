package itb2.gui;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class DraggableScrollPane extends JScrollPane {
	private static final long serialVersionUID = 7574613713592839284L;
	private final MouseMotionListener dragListener;
	private final MouseListener clickListener;
	private Point prev;
	
	public DraggableScrollPane(Component comp) {
		this(comp, MouseEvent.BUTTON1_DOWN_MASK);
	}
	
	public DraggableScrollPane(Component comp, int dragMask) {
		super(comp);
		
		setWheelScrollingEnabled(false);
		
		clickListener = new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			
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
	
	private void registerEventListener(Component comp) {
		comp.addMouseListener(clickListener);
		comp.addMouseMotionListener(dragListener);
	}
	
	private void removeEventListener(Component comp) {
		comp.removeMouseListener(clickListener);
		comp.removeMouseMotionListener(dragListener);
	}
	
}
