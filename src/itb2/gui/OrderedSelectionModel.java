package itb2.gui;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

/**
 * {@link ListSelectionModel} which remembers the order of selection.
 *
 * @author Micha Strauch
 */
public class OrderedSelectionModel extends DefaultListSelectionModel {
	private static final long serialVersionUID = -4357149423934327138L;
	
	/** Set with selected indexes, which keeps the order of inserts */
	private final LinkedHashSet<Integer> selected = new LinkedHashSet<>();
	
	/**
	 * Returns an array of all selected indexes in the order of selection. 
	 * 
	 * @return All selected indexes, in order of selection
	 */
	public int[] getSelectedIndexes() {
		int[] selectedIndexes = new int[selected.size()];
		
		int i = 0;
		for(int index : selected)
			selectedIndexes[i++] = index;
		
		return selectedIndexes;
	}

	@Override
	public void addSelectionInterval(int index0, int index1) {
		int dir = index0 < index1 ? 1 : -1;
		int start = index0, stop = index1 + dir;
		
		for(int i = start; i != stop; i += dir)
			selected.add(i);
		
		super.addSelectionInterval(index0, index1);
	}

	@Override
	public void clearSelection() {
		selected.clear();

		super.clearSelection();
	}

	@Override
	public void insertIndexInterval(int index, int length, boolean before) {
		int start = before ? index : index + 1;
		List<Integer> newIndexes = new ArrayList<>(selected.size());
		
		for(int i : selected) {
			if(i >= start)
				i += length;
			newIndexes.add(i);
		}
		
		selected.clear();
		selected.addAll(newIndexes);

		super.insertIndexInterval(index, length, before);
	}

	@Override
	public void removeIndexInterval(int index0, int index1) {
		int start = index0 < index1 ? index0 : index1;
		int end = index0 > index1 ? index0 : index1;
		int length = end - start + 1;
		
		List<Integer> newIndexes = new ArrayList<>(selected.size());
		
		for(int i : selected) {
			if(i >= start && i <= end)
				continue;
			if(i > end)
				i -= length;
			newIndexes.add(i);
		}
		
		selected.clear();
		selected.addAll(newIndexes);

		super.removeIndexInterval(index0, index1);
	}

	@Override
	public void removeSelectionInterval(int index0, int index1) {
		int dir = index0 < index1 ? 1 : -1;
		int start = index0, stop = index1 + dir;
		
		for(int i = start; i != stop; i += dir)
			selected.remove(i);
		
		super.removeSelectionInterval(index0, index1);
	}

	@Override
	public void setSelectionInterval(int index0, int index1) {
		selected.clear();
		
		int dir = index0 < index1 ? 1 : -1;
		int start = index0, stop = index1 + dir;
		
		for(int i = start; i != stop; i += dir)
			selected.add(i);
		
		super.setSelectionInterval(index0, index1);
	}

	@Override
	public void setSelectionMode(int selectionMode) {
		switch(selectionMode) {
			case MULTIPLE_INTERVAL_SELECTION:
				super.setSelectionMode(selectionMode);
				break;
			case SINGLE_SELECTION:
			case SINGLE_INTERVAL_SELECTION:
				throw new UnsupportedOperationException("Unsupported selection mode");
			default:
				throw new IllegalArgumentException("invalid selectionMode");
		}
	}

}
