package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionListener;

import itb2.data.ObservableTreeSet;
import itb2.engine.Controller;
import itb2.filter.Filter;

public class FilterList extends JPanel {
	private static final long serialVersionUID = 977279295491172369L;
	private final JList<Filter> filterList;
	private final FilterContext context;
	
	public FilterList(EditorGui gui) {
		context = new FilterContext(gui);
		
		filterList = new JList<>();
		filterList.setModel(new FilterModel());
		filterList.setCellRenderer(new FilterRenderer());
		
		filterList.addMouseListener(new MouseListener() {
			@Override public void mouseReleased(MouseEvent e) {}
			@Override public void mousePressed(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					int index = filterList.locationToIndex(e.getPoint());
					if(index >= 0)
						filterList.setSelectedIndex(index);
					context.show(FilterList.this, e.getX(), e.getY());
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(filterList);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		
		setBackground(Color.BLUE);
	}
	
	public Filter getSelectedFilter() {
		return filterList.getSelectedValue();
	}
	
	public void addSelectionListener(ListSelectionListener listener) {
		filterList.addListSelectionListener(listener);
	}
	
	private class FilterRenderer extends JLabel implements ListCellRenderer<Filter> {
		private static final long serialVersionUID = 1442148494574959747L;
		
		public FilterRenderer() {
			setBackground(Color.LIGHT_GRAY);
			Font font = getFont();
			font = font.deriveFont(font.getSize() + 1.1f);
			font = font.deriveFont(font.getStyle() & (Font.BOLD ^ -1));
			setFont(font);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Filter> list, Filter value, int index, boolean isSelected, boolean cellHasFocus) {
			setText(value.getClass().getSimpleName());
			setOpaque(isSelected);
			return this;
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
		public void addListDataListener(ListDataListener l) {
			listeners.add(l);
		}

		@Override
		public Filter getElementAt(int index) {
			return (Filter)filters.toArray()[index];
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
