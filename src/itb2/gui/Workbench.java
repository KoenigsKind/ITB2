package itb2.gui;

import java.awt.CardLayout;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import itb2.image.Image;

public class Workbench extends JPanel {
	private static final long serialVersionUID = 1991777977948041657L;
	private static final String SINGLE_IMAGE = "SingleImage", MULTIPLE_IMAGE = "MultipleImage";
	private final SingleImagePainter singleImagePainter;
	private final MultiImagePainter multiImagePainter;
	private final DraggableScrollPane scrollPane;
	private final CardLayout layout;
	private final JFrame gui;
	
	public Workbench(JFrame gui, ImageList imageList) {
		this.gui = gui;
		
		// Single image
		singleImagePainter = new SingleImagePainter();
		scrollPane = new DraggableScrollPane(singleImagePainter);
		
		// Multiple image
		multiImagePainter = new MultiImagePainter();
		
		layout = new CardLayout();
		setLayout(layout);
		add(scrollPane, SINGLE_IMAGE);
		add(multiImagePainter, MULTIPLE_IMAGE);
		
		imageList.addSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				List<Image> selected = imageList.getSelectedImages();
				Image[] images = selected.toArray(new Image[selected.size()]);
				show(images);
			}
		});
	}
	
	public void show(Image... images) {
		String name = "";
		
		if(images.length == 1) {
			singleImagePainter.setImage(images[0]);
			layout.show(this, SINGLE_IMAGE);
			
			if(images[0].getName() != null) {
				if(images[0].getName() instanceof File)
					name = ((File)images[0].getName()).getName();
				else
					name = images[0].getName().toString();
			}
		} else {
			multiImagePainter.setImages(images);
			layout.show(this, MULTIPLE_IMAGE);
		}
		
		gui.setTitle(name);
	}
	
	public void resetZoom() {
		singleImagePainter.setZoom(1);
	}
	
	public void fitToScreen() {
		Image image = singleImagePainter.getImage();
		if(image != null) {
			int imgWidth = image.getWidth();
			int imgHeight = image.getHeight();
			
			double border = scrollPane.getVerticalScrollBar().getWidth();

			double zoomHor = (getWidth() - border) / imgWidth;
			double zoomVert = (getHeight() - border) / imgHeight;
			double zoom = zoomHor < zoomVert ? zoomHor : zoomVert;
			singleImagePainter.setZoom(zoom);
		} else
			singleImagePainter.setZoom(1);
	}
}
