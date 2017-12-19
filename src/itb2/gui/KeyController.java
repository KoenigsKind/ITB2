package itb2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * Sets up all shortcuts
 *
 * @author Micha Strauch
 */
class KeyController {
	
	/**
	 * Registers all shortcuts for the given editor gui
	 * 
	 * @param gui EditorGui to register shortcuts for
	 */
	public static void registerShortcuts(EditorGui gui) {
		register(gui.getImageList(), Shortcut.DELETE_ITEM.key, e -> gui.closeImage());
		register(gui.getFilterList(), Shortcut.DELETE_ITEM.key, e -> gui.closeFilter());
		
		register(gui.getRootPane(), Shortcut.LOG.key, e -> gui.toggleLogFrame());
		register(gui.getLogFrame().getRootPane(), Shortcut.LOG.key, e -> gui.toggleLogFrame());
		
		register(gui.getRootPane(), Shortcut.IMAGE_OPEN.key, e -> gui.openImage());
		register(gui.getRootPane(), Shortcut.IMAGE_SAVE.key, e -> gui.saveImage());
		register(gui.getRootPane(), Shortcut.IMAGE_CLOSE.key, e -> gui.closeImage());
		
		register(gui.getRootPane(), Shortcut.FILTER_OPEN.key, e -> gui.openFilter());
		register(gui.getRootPane(), Shortcut.FILTER_RUN.key, e -> gui.runFilter());
		register(gui.getRootPane(), Shortcut.FILTER_CLOSE.key, e -> gui.closeFilter());
		
		register(gui.getRootPane(), Shortcut.RESET_ZOOM.key, e -> gui.resetZoom());
		register(gui.getRootPane(), Shortcut.FIT_TO_SCREEN.key, e -> gui.fitToScreen());
	}
	
	/**
	 * Registers shortcut at given component
	 * 
	 * @param comp      Component to register shortcut at
	 * @param keyStroke Shortcut to register
	 * @param action    Action to run once shortcut is triggered
	 */
	private static void register(JComponent comp, KeyStroke keyStroke, Consumer<ActionEvent> action) {
		Object key = new Object();
		comp.getActionMap().put(key, new AbstractAction() {
			private static final long serialVersionUID = 8166481245188324629L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				action.accept(e);
			}
		});
		comp.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, key);
	}
	
	/**
	 * All known shortcuts
	 *
	 * @author Micha Strauch
	 */
	public enum Shortcut {
		DELETE_ITEM(KeyEvent.VK_DELETE, 0, "Del"),
		LOG(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK, "Ctrl + L"),
		IMAGE_OPEN(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK, "Ctrl + O"),
		IMAGE_SAVE(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK, "Ctrl + S"),
		IMAGE_CLOSE(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK, "Ctrl + D"),
		FILTER_OPEN(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK, "Ctrl + Shift + O"),
		FILTER_CLOSE(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK, "Ctrl + Shift + D"),
		FILTER_RUN(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK, "Ctrl + Enter"),
		RESET_ZOOM(KeyEvent.VK_F2, 0, "F2"),
		FIT_TO_SCREEN(KeyEvent.VK_F3, 0, "F3");
		
		/** Shortcut */
		public final KeyStroke key;
		/** Shortcut in text form */
		public final String description;
		
		/** Constructor for a shortcut */
		private Shortcut(int keyCode, int modifiers, String description) {
			this.key = KeyStroke.getKeyStroke(keyCode, modifiers);
			this.description = description;
		}
		
		
	}

}
