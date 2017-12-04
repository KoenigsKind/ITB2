package itb2.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import itb2.engine.Controller;

/**
 * Loads filters using drag'n'drop
 *
 * @author Micha Strauch
 */
class FilterTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 8706811120575226065L;
	
	@Override
	public boolean canImport(TransferSupport support) {
		return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return NONE;
	}
	
	@Override
	public boolean importData(TransferSupport support) {
		try {
			Transferable transferable = support.getTransferable();
				
			if(transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				@SuppressWarnings("unchecked")
				List<File> data = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
				
				for(File file : data)
					Controller.getFilterManager().loadFilter(file);
				
				return true;
			}
		} catch(UnsupportedFlavorException|IOException e) {
			Controller.getCommunicationManager().error("Could not handle DnD\n" + e.getMessage());
		}
		return false;
	}

}
