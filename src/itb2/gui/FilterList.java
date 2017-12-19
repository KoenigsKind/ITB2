package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;

import itb2.data.ObservableTreeSet;
import itb2.engine.Controller;
import itb2.engine.io.FilterWrapper;
import itb2.filter.Filter;
import itb2.filter.RequireImageType;
import itb2.image.Image;

public class FilterList extends JPanel {
	private static final long serialVersionUID = 977279295491172369L;
	private final JList<Filter> filterList;
	
	public FilterList(EditorGui gui) {
		filterList = new JList<>();
		filterList.setModel(new FilterModel());
		filterList.setCellRenderer(new FilterRenderer());
		filterList.setBackground(GuiConstants.DEFAULT_BACKGROUND);
		filterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		filterList.setTransferHandler(new FilterTransferHandler());
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(filterList);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public Filter getSelectedFilter() {
		return filterList.getSelectedValue();
	}
	
	public void addSelectionListener(ListSelectionListener listener) {
		filterList.addListSelectionListener(listener);
	}
	
	public void addListDataListener(ListDataListener listener) {
		filterList.getModel().addListDataListener(listener);
	}
	
	private class FilterRenderer extends JLabel implements ListCellRenderer<Filter> {
		private static final long serialVersionUID = 1442148494574959747L;

		
		/** Border for focused and non focused filters */
		private final Border focused, nonfocused;
		
		public FilterRenderer() {
			setBackground(Color.LIGHT_GRAY);
			Font font = getFont();
			font = font.deriveFont(font.getSize() + 1.1f);
			font = font.deriveFont(font.getStyle() & (Font.BOLD ^ -1));
			setFont(font);
			
			focused = BorderFactory.createDashedBorder(Color.WHITE, 2, 7);
			nonfocused = null;
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Filter> list, Filter value, int index, boolean isSelected, boolean cellHasFocus) {
			if(value instanceof FilterWrapper)
				setText(((FilterWrapper) value).getWrappedClass().getSimpleName());
			else
				setText(value.getClass().getSimpleName());
			setOpaque(isSelected);
			setBorder(cellHasFocus ? focused : nonfocused);
			
			String requiredImageType = getRequiredImageType(value);
			setToolTipText("<html><b>Required image type</b><br>" + requiredImageType + "</html>");
			
			return this;
		}
		
		/**
		 * Returns the required image type for this filter, or null if not specified
		 * 
		 * @param filter Filter to get image type for
		 * @return Required image type
		 */
		private String getRequiredImageType(Filter filter) {
			RequireImageType require = filter.getClass().getAnnotation(RequireImageType.class);
			Class<? extends Image> image = require != null ? require.value() : null;
			return image != null ? image.getSimpleName() : "-- none --";
		}
		
	}
	
	private class FilterModel implements ListModel<Filter> {
		private final ObservableTreeSet<Filter> filters;
		private final Set<ListDataListener> listeners;
		
		FilterModel() {
			filters = Controller.getFilterManager().getFilters();
			listeners = new HashSet<>();
			filters.addListener(new ObservableTreeSet.SetListener<Filter>() {

				@Override
				public void itemsChanged(ObservableTreeSet<Filter> set, int type) {
					ListDataEvent event = new ListDataEvent(FilterModel.this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
					
					for(ListDataListener listener : listeners)
						listener.contentsChanged(event);
				}
				
			});
		}

		@Override
		public void addListDataListener(ListDataListener listener) {
			listeners.add(listener);
		}

		@Override
		public Filter getElementAt(int index) {
			try {
				return (Filter)filters.toArray()[index];
			} catch(ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}

		@Override
		public int getSize() {
			return filters.size();
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}
		
	}

}
