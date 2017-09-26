package itb2.engine;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.UIManager;

import itb2.gui.CommunicationManagerImpl;
import itb2.gui.EditorGui;

/**
 * Controller providing managers and starting application
 * 
 * @author Micha Strauch
 */
public final class Controller {
	
	/** Manager for filters */
	private static FilterManager filterManager;
	
	/** Manager for images */
	private static ImageManager imageManager;
	
	/** Manager for communication */
	private static CommunicationManager communicationManager;
	
	/** GUI */
	private static EditorGui gui;
	
	/** Returns the filter manager */
	public static FilterManager getFilterManager() {
		if(filterManager == null)
			filterManager = new FilterManagerImpl();
		
		return filterManager;
	}
	
	/** Returns the image manager */
	public static ImageManager getImageManager() {
		if(imageManager == null)
			imageManager = new ImageManagerImpl();
		
		return imageManager;
	}
	
	/** Returns the communication manager */
	public static CommunicationManager getCommunicationManager() {
		return communicationManager;
	}
	
	/** Sets the communication manager */
	protected static void setCommunicationManager(CommunicationManager communicationManager) {
		Controller.communicationManager = communicationManager;
	}
	
	/** Starts the application */
	public static void startApplication() {
		if(gui != null)
			throw new RuntimeException("Application already running!");
		
		try {
			String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(systemLookAndFeel);
		} catch(Exception e) {
			// Not important, just for visuals
		}
		
		gui = new EditorGui();
		gui.setDefaultCloseOperation(EditorGui.EXIT_ON_CLOSE);
		
		setCommunicationManager(new CommunicationManagerImpl(gui));
		
		gui.setVisible(true);
	}
	
	/** Sets logger-options and starts application */
	public static void main(String[] args) {
		// Log all levels, but not to System.out
		LogManager.getLogManager().reset();
		Logger.getGlobal().setLevel(Level.ALL);
		
		// Start application
		startApplication();
	}
	
}
