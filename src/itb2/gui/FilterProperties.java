package itb2.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import itb2.filter.Filter;
import itb2.filter.property.FilterProperty;
import itb2.gui.property.PropertyBuilder;

public class FilterProperties extends JPanel {
	private static final long serialVersionUID = 8059230545512023386L;
	private final FilterList filterList;
	private final JPanel propertyPanel;
	private boolean isEmpty = true;
	double divider = 0.5;
	
	public FilterProperties(FilterList filterList) {
		this.filterList = filterList;
		
		propertyPanel = new JPanel(){
			private static final long serialVersionUID = -3302191723332743272L;

			@Override
			public Component add(Component comp) {
				if(comp instanceof JComponent)
					((JComponent) comp).setAlignmentX(0);
				
				// Stop vertical resizing
				Dimension max = comp.getMaximumSize();
				max.height = comp.getPreferredSize().height;
				comp.setMaximumSize(max);
				
				// Ignore preferred width
				Dimension pref = comp.getPreferredSize();
				pref.width = 0;
				comp.setPreferredSize(pref);
				
				add(comp, 0.0);
				return comp;
			}
		};
		propertyPanel.setLayout(new BoxLayout(propertyPanel, BoxLayout.Y_AXIS));
		propertyPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane(propertyPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
		
		filterList.addSelectionListener(e -> {
			if(!e.getValueIsAdjusting())
				filterChanged();
		});
		filterList.addListDataListener(new ListDataListener() {
			@Override
			public void intervalRemoved(ListDataEvent e) {
				filterChanged();
			}
			
			@Override
			public void intervalAdded(ListDataEvent e) {
				filterChanged();
			}
			
			@Override
			public void contentsChanged(ListDataEvent e) {
				filterChanged();
			}
		});
	}
	
	private void filterChanged() {
		Filter filter = filterList.getSelectedFilter();
		
		ArrayList<FilterProperty> properties = new ArrayList<>();
		if(filter != null)
			properties.addAll(filter.getProperties().getProperties());
		properties.sort((a, b) -> a.index - b.index);
		buildPanel(properties);
	}
	
	public void buildPanel(List<FilterProperty> properties) {
		boolean wasEmpty = isEmpty;
		isEmpty = properties.isEmpty();
		
		propertyPanel.removeAll();
		for(FilterProperty property : properties) {
			PropertyBuilder.buildProperty(property, propertyPanel);
			propertyPanel.add(Box.createVerticalStrut(5));
		}
		revalidate();
		repaint();
		
		if(isEmpty || wasEmpty) try {
			JSplitPane splitPane = (JSplitPane) getParent();
			if(isEmpty && !wasEmpty)
				divider = (double) splitPane.getDividerLocation() / splitPane.getHeight();
			splitPane.setDividerLocation(isEmpty ? 0.999 : divider);
		} catch(Exception e) {
			// Do nothing, it's just for  appearance
		}
	}

}
