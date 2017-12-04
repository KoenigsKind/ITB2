package itb2.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import itb2.data.ObservableLinkedList;
import itb2.engine.Controller;
import itb2.engine.io.ImageIO;
import itb2.image.Image;

/**
 * Loads images using drag'n'drop and handles sorting the given {@link JList}
 *
 * @author Micha Strauch
 */
class ImageTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 8706811120575226065L;
	
	/** DataFlavor for {@link Image images} */
	private static final DataFlavor imageFlavor = new DataFlavor(Image[].class, "ITB2 Images");
	
	/** JList displaying images */
	private final JList<Image> list;
	
	/** Indexes of selected images */
	private int[] moveFrom;
	
	/** Insertion index */
	private int moveTo;
	
	/**
	 * Constructor
	 * 
	 * @param list The JList, this TransferHandler handles sorting for.
	 */
	public ImageTransferHandler(JList<Image> list) {
		this.list = list;
	}
	
	@Override
	public boolean canImport(TransferSupport support) {
		if(support.isDataFlavorSupported(imageFlavor))
			return true;
		if(support.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			return true;
		
		return false;
	}
	
	@Override
	protected Transferable createTransferable(JComponent c) {
		moveFrom = list.getSelectedIndices();
		List<Image> images = list.getSelectedValuesList();
		return new ImageTransferable(images.toArray(new Image[images.size()]));
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return MOVE;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		JList.DropLocation dropLocation = (JList.DropLocation) support.getDropLocation();
		
		ObservableLinkedList<Image> imageList = Controller.getImageManager().getImageList();
		int index = dropLocation.getIndex();
		if(index < 0)
			index = imageList.size();
		
		try {
			List<Image> images;
			
			Transferable transferable = support.getTransferable();
			if(transferable.isDataFlavorSupported(imageFlavor)) {
				
				Image[] data = (Image[]) transferable.getTransferData(imageFlavor);
				images = Arrays.asList(data);
				moveTo = index;
				
			} else if(transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				
				images = new LinkedList<>();
				
				@SuppressWarnings("unchecked")
				List<File> data = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
				for(File file : data)
					images.add(ImageIO.load(file));
				moveTo = -1;
				
			} else
				return false;
			
			imageList.addAll(index, images);
			return true;
		} catch(UnsupportedFlavorException|IOException e) {
			Controller.getCommunicationManager().error("Could not handle DnD\n" + e.getMessage());
			return false;
		}
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		if(action != MOVE || moveTo < 0)
			return;
		
		ObservableLinkedList<Image> list = Controller.getImageManager().getImageList();
		
		for(int i = moveFrom.length - 1; i >= 0; i--) {
			int index = moveFrom[i];
			if(index >= moveTo)
				index += moveFrom.length;
			
			list.remove(index);
		}
	}
	
	/**
	 * Transferable for {@link Image images}
	 *
	 * @author Micha Strauch
	 */
	private class ImageTransferable implements Transferable {
		/** Images to be transfered */
		private final Image[] images;
		
		/**
		 * Constructor
		 * 
		 * @param images Images to be transfered
		 */
		public ImageTransferable(Image[] images) {
			this.images = images;
		}

		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if(isDataFlavorSupported(flavor))
				return images;
			
			throw new UnsupportedFlavorException(flavor);
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] {imageFlavor};
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return imageFlavor.equals(flavor);
		}
		
	}

}
