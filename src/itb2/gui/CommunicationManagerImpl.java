package itb2.gui;

import javax.swing.JOptionPane;

import itb2.engine.CommunicationManager;

public class CommunicationManagerImpl implements CommunicationManager {
	private final EditorGui gui;
	
	public CommunicationManagerImpl(EditorGui gui) {
		this.gui = gui;
	}

	@Override
	public void info(String message, Object... param) {
		if(param.length > 0)
			message = String.format(message, param);
		
		//TODO Use a log-window
		JOptionPane.showMessageDialog(gui, message, EditorGui.TITLE + " - Info", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void warning(String message, Object... param) {
		if(param.length > 0)
			message = String.format(message, param);
		
		//TODO Use a log-window
		JOptionPane.showMessageDialog(gui, message, EditorGui.TITLE + " - Warning", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void error(String message, Object... param) {
		if(param.length > 0)
			message = String.format(message, param);
		
		JOptionPane.showMessageDialog(gui, message, EditorGui.TITLE + " - Error", JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void inProgress(double percent) {
		// TODO Display filter progress
	}

	@Override
	public void inProgress(double percent, String message, Object... param) {
		// TODO Display filter progress
	}

}
