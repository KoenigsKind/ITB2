package itb2.gui;

import java.awt.Color;

import javax.swing.JPanel;

public class FilterProperties extends JPanel {
	private static final long serialVersionUID = 8059230545512023386L;
	private final EditorGui editorGui;
	
	public FilterProperties(EditorGui editorGui) {
		this.editorGui = editorGui;
		
		setBackground(Color.YELLOW);
	}

}
