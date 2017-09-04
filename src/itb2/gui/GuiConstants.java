package itb2.gui;

import java.awt.Color;

import javax.swing.UIManager;

public class GuiConstants {
	public static final Color DEFAULT_BACKGROUND, WORKBENCH_BACKGROUND;
	
	static {
		DEFAULT_BACKGROUND = UIManager.getColor("Panel.background");
		WORKBENCH_BACKGROUND = Color.WHITE;
	}
}
