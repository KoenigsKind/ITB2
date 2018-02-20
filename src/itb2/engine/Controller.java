package itb2.engine;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import itb2.engine.io.Config;
import itb2.gui.DefaultCommunicationManager;
import itb2.gui.EditorGui;
import itb2.gui.MessageType;

/**
 * Controller providing managers and starting application
 * 
 * @author Micha Strauch
 */
public final class Controller {
	
	/** Strong reference to logger, to keep logger alive */
	private static Logger logger;
	
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
			filterManager = new DefaultFilterManager();
		
		return filterManager;
	}
	
	/** Returns the image manager */
	public static ImageManager getImageManager() {
		if(imageManager == null)
			imageManager = new DefaultImageManager();
		
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
	public static void startApplication(String... args) {
		if(gui != null)
			throw new RuntimeException("Application already running!");
		
		// Parse arguments
		ArgumentParser parser = ArgumentParser.parse(args);
		if(parser == null)
			return;
		
		// Initialize logger; do not print to System.out
		logger = Logger.getLogger("ITB2");
		logger.setLevel(MessageType.INFO.level);
		logger.setUseParentHandlers(false);
		
		// Setup GUI
		try {
			String systemLookAndFeel = UIManager.getSystemLookAndFeelClassName();
			UIManager.setLookAndFeel(systemLookAndFeel);
		} catch(Exception e) {
			// Not important, just for visuals
		}
		
		gui = new EditorGui();
		gui.setDefaultCloseOperation(EditorGui.EXIT_ON_CLOSE);
		
		setCommunicationManager(new DefaultCommunicationManager(gui));
		
		// Register image conversions for all basic image types
		if(parser.useConversionHelper())
			ConversionHelper.registerImageConversions();
		
		// Remember state when closing window for next time
		if(parser.getConfigFile() != null) {
			gui.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent we) {
					try {
						Config.saveState(parser.getConfigFile());
					} catch (IOException e) {
						getCommunicationManager().warning("Could not save config: " + e.getMessage());
					}
				}
			});
			try {
				Config.loadState(parser.getConfigFile());
			} catch (FileNotFoundException e) {
				// Ignore, config might not have been saved yet
			} catch (Exception e) {
				getCommunicationManager().warning("Could not load config file: " + e.getMessage());
			} catch (Throwable e) {
				// Severe problem
				String message = String.format("Broken config file, please delete '%s'!", parser.getConfigFile().getPath());
				System.err.println(message);
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, message, "Broken config", JOptionPane.ERROR_MESSAGE);
				// Don't continue, internal values might be set incorrectly
				System.exit(-1);
			}
		}
		
		gui.setVisible(true);
	}
	
	/** Starts application */
	public static void main(String[] args) {
		// Start application
		startApplication(args);
	}
	
	/** Should not be instantiated */
	private Controller() {}
	
}
