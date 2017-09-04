package itb2.gui;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JScrollPane;

public class DraggableScrollPane extends JScrollPane {
	private static final long serialVersionUID = 7574613713592839284L;
	private int mouseX, mouseY;
	
	public DraggableScrollPane(Component comp) {
		this(comp, MouseEvent.BUTTON1_DOWN_MASK);
	}
	
	public DraggableScrollPane(Component comp, int dragMask) {
		super(comp);
		
		setWheelScrollingEnabled(false);
		addMouseListener(new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseClicked(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("Pressed");
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override public void mouseMoved(MouseEvent e) {}
			
			@Override public void mouseDragged(MouseEvent e) {
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
		});
	}
}
