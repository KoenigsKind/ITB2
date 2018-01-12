package itb2.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import itb2.engine.Controller;
import itb2.engine.io.FilterIO;
import itb2.engine.io.ImageIO;
import itb2.image.BinaryImage;
import itb2.image.DrawableImage;
import itb2.image.GrayscaleImage;
import itb2.image.GroupedImage;
import itb2.image.HsiImage;
import itb2.image.HsvImage;
import itb2.image.Image;
import itb2.image.ImageConverter;
import itb2.image.ImageFactory;
import itb2.image.RgbImage;

public class EditorMenuBar extends JMenuBar {
	private static final long serialVersionUID = -217686192194157463L;
	private static final String TYPE_FILTER = "filter", TYPE_IMAGE = "image";
	private final EditorGui gui;
	private final JMenu imageMenu, filterMenu, converterMenu;
	
	public EditorMenuBar(EditorGui gui) {
		this.gui = gui;
		
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
		
		// Build converter menu
		converterMenu = new JMenu("Convert Image");
		converterMenu.add(getConverter("RGB", RgbImage.class));
		converterMenu.add(getConverter("HSI", HsiImage.class));
		converterMenu.add(getConverter("HSV", HsvImage.class));
		converterMenu.addSeparator();
		converterMenu.add(getConverter("Grayscale", GrayscaleImage.class));
		converterMenu.add(getConverter("Binary", BinaryImage.class));
		converterMenu.addSeparator();
		converterMenu.add(getConverter("Drawable", DrawableImage.class));
		converterMenu.add(getConverter("Grouped", GroupedImage.class));
		converterMenu.addSeparator();
		
		JMenu converterByteMenu = new JMenu("Byte Precision");
		converterByteMenu.add(getConverter("RGB", ImageFactory.bytePrecision().rgb()));
		converterByteMenu.add(getConverter("HSI", ImageFactory.bytePrecision().hsi()));
		converterByteMenu.add(getConverter("HSV", ImageFactory.bytePrecision().hsv()));
		converterByteMenu.addSeparator();
		converterByteMenu.add(getConverter("Grayscale", ImageFactory.bytePrecision().gray()));
		converterByteMenu.add(getConverter("Binary", ImageFactory.bytePrecision().binary()));
		converterByteMenu.addSeparator();
		converterByteMenu.add(getConverter("Drawable", ImageFactory.bytePrecision().drawable()));
		converterByteMenu.add(getConverter("Grouped", ImageFactory.bytePrecision().group()));
		
		JMenu converterDoubleMenu = new JMenu("Double Precision");
		converterDoubleMenu.add(getConverter("RGB", ImageFactory.doublePrecision().rgb()));
		converterDoubleMenu.add(getConverter("HSI", ImageFactory.doublePrecision().hsi()));
		converterDoubleMenu.add(getConverter("HSV", ImageFactory.doublePrecision().hsv()));
		converterDoubleMenu.addSeparator();
		converterDoubleMenu.add(getConverter("Grayscale", ImageFactory.doublePrecision().gray()));
		converterDoubleMenu.addSeparator();
		converterDoubleMenu.add(getConverter("Grouped", ImageFactory.doublePrecision().group()));
		
		converterMenu.add(converterByteMenu);
		converterMenu.add(converterDoubleMenu);
		add(converterMenu);
		
		
	}
	
	private JMenuItem getConverter(String text, Class<? extends Image> destination) {
		JMenuItem item = new JMenuItem(text);
		
		item.addActionListener(e -> {
			try {	
				List<Image> input = gui.getImageList().getSelectedImages();
				List<Image> output = new ArrayList<>(input.size());
				
				for(Image image : input)
					output.add( ImageConverter.convert(image, destination) );
				
				Controller.getImageManager().getImageList().addAll(output);
				
			} catch(Exception ex) {
				Controller.getCommunicationManager().error("Error while converting images:\n%s", ex.getMessage());
			}
		});
		
		return item;
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
