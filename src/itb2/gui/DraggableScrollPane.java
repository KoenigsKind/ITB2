package itb2.gui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class DraggableScrollPane extends JScrollPane {
	private static final long serialVersionUID = 7574613713592839284L;
	private final int dragMask;
	private int mouseX, mouseY;
	
	public DraggableScrollPane(Component comp) {
		this(comp, MouseEvent.BUTTON1_DOWN_MASK);
	}
	
	public DraggableScrollPane(Component comp, int dragMask) {
		super(comp);
		
		this.dragMask = dragMask;
		
		setWheelScrollingEnabled(false);
		
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			
			@Override
			public void eventDispatched(AWTEvent event) {
				if(event instanceof MouseEvent) {
					MouseEvent mouseEvent = (MouseEvent) event;
					
					Component comp = mouseEvent.getComponent();
					if(!SwingUtilities.isDescendingFrom(comp, DraggableScrollPane.this))
						return;
					
					if(mouseEvent.getID() == MouseEvent.MOUSE_PRESSED)
						handleMousePressed(mouseEvent);
					
					if(mouseEvent.getID() == MouseEvent.MOUSE_DRAGGED)
						handleMouseDragged(mouseEvent);
				}
			}
			
		}, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}
	
	private void handleMousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	private void handleMouseDragged(MouseEvent e) {
		if((e.getModifiersEx() & dragMask) == dragMask) {
			int dX = mouseX - e.getX();
			int dY = mouseY - e.getY();
			
			Rectangle rect = getViewport().getVisibleRect();
			rect.x += dX;
			rect.y += dY;
			getViewport().scrollRectToVisible(rect);
			
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}
}
