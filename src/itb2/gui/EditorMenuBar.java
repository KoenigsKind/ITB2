package itb2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import itb2.engine.Controller;
import itb2.engine.io.FilterIO;
import itb2.engine.io.ImageIO;

public class EditorMenuBar extends JMenuBar {
	private static final long serialVersionUID = -217686192194157463L;
	private static final String TYPE_FILTER = "filter", TYPE_IMAGE = "image";
	private final JMenu imageMenu, filterMenu;
	
	public EditorMenuBar(EditorGui gui) {
		// Build image menu
		imageMenu = new JMenu("Last Images");
		add(imageMenu);
		
		// Build filter menu
		filterMenu = new JMenu("Last Filters");
		add(filterMenu);
		
		// Add menu items and register listener
		synchronized (ImageIO.getLastImages()) {
			ImageIO.getLastImages().addListener(new Listener(imageMenu, TYPE_IMAGE));
			
			for(File file : ImageIO.getLastImages())
				imageMenu.add(new Item(file, TYPE_IMAGE));
		}
		
		synchronized (FilterIO.getLastFilters()) {
			FilterIO.getLastFilters().addListener(new Listener(filterMenu, TYPE_FILTER));
			
			for(File file : FilterIO.getLastFilters())
				filterMenu.add(new Item(file, TYPE_FILTER));
		}
	}
	
	private class Listener implements ListDataListener {
		final JMenu menu;
		final String type;
		
		Listener(JMenu menu, String type) {
			this.menu = menu;
			this.type = type;
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			List<?> list = (List<?>)e.getSource();
			
			for(int i = e.getIndex1(); i >= e.getIndex0(); i--) {
				File file = (File)list.get(i);
				menu.remove(i);
				menu.insert(new Item(file, type), i);
			}
		}

		@Override
		public void intervalAdded(ListDataEvent e) {
			List<?> list = (List<?>)e.getSource();
			
			for(int i = e.getIndex0(); i <= e.getIndex1(); i++) {
				File file = (File)list.get(i);
				menu.insert(new Item(file, type), i);
			}
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			for(int i = e.getIndex1(); i >= e.getIndex0(); i--)
				menu.remove(i);
		}
	}
	
	private class Item extends JMenuItem {
		private static final long serialVersionUID = 8849058756010952698L;

		public Item(File file, String type) {
			super(file.getPath());
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if(type == TYPE_FILTER)
							Controller.getFilterManager().loadFilter(file);
						else if(type == TYPE_IMAGE)
							Controller.getImageManager().loadImage(file);
					} catch(Exception ex) {
						Controller.getCommunicationManager().error("Could not open %s: '%s'", type, file.getName());
					}
				}
			});
		}
		
	}

}
