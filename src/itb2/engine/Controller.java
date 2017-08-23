package itb2.engine;

import itb2.gui.EditorGui;

public final class Controller {
	private static FilterManager filterManager;
	private static ImageManager imageManager;
	private static CommunicationManager communicationManager;
	
	public static FilterManager getFilterManager() {
		if(filterManager == null)
			filterManager = new FilterManagerImpl();
		
		return filterManager;
	}
	
	public static ImageManager getImageManager() {
		if(imageManager == null)
			imageManager = new ImageManagerImpl();
		
		return imageManager;
	}
	
	public static CommunicationManager getCommunicationManager() {
		return communicationManager;
	}
	
	public static void startApplication() {
		EditorGui gui = new EditorGui();
		gui.setDefaultCloseOperation(EditorGui.EXIT_ON_CLOSE);
		gui.setVisible(true);
	}
	
	public static void main(String[] args) {
		startApplication();
	}
	
}
