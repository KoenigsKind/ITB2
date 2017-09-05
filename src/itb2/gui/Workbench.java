package itb2.gui;

import java.awt.CardLayout;
import java.util.List;

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
	
	public Workbench(ImageList imageList) {
		
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
				if(selected.size() == 1) {
					singleImagePainter.setImage(selected.get(0));
					layout.show(Workbench.this, SINGLE_IMAGE);
				} else {
					multiImagePainter.setImages(selected);
					layout.show(Workbench.this, MULTIPLE_IMAGE);
				}
			}
		});
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
