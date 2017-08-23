package itb2.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class ImageList extends JPanel {
	private static final long serialVersionUID = -7831547959723130632L;
	private final EditorGui editorGui;
	
	public ImageList(EditorGui editorGui) {
		this.editorGui = editorGui;
		
		setBackground(Color.GREEN);
		setPreferredSize(new Dimension(100, 0));
	}

}
