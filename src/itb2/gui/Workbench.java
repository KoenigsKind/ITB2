package itb2.gui;

import java.awt.Color;

import javax.swing.JPanel;

public class Workbench extends JPanel {
	private static final long serialVersionUID = 1991777977948041657L;
	private final EditorGui editorGui;
	
	public Workbench(EditorGui editorGui) {
		this.editorGui = editorGui;
		
		setBackground(Color.RED);
	}
	
}
