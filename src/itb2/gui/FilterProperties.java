package itb2.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import itb2.filter.Filter;
import itb2.filter.property.FilterProperty;
import itb2.gui.property.PropertyBuilder;

public class FilterProperties extends JPanel {
	private static final long serialVersionUID = 8059230545512023386L;
	
	public FilterProperties(FilterList filterList) {
		filterList.addSelectionListener(e -> filterChanged(e, filterList));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		setBackground(Color.YELLOW);
	}
	
	private void filterChanged(ListSelectionEvent e, FilterList list) {
		if(e.getValueIsAdjusting())
			return;
		
		Filter filter = list.getSelectedFilter();
		
		ArrayList<FilterProperty> properties = new ArrayList<>();
		properties.addAll(filter.getProperties().getProperties());
		properties.sort((a, b) -> b.index - a.index);
		buildPanel(properties);
	}
	
	public void buildPanel(List<FilterProperty> properties) {
		removeAll();
		for(FilterProperty property : properties) {
			JPanel propertyPanel = PropertyBuilder.buildProperty(property);
			add(propertyPanel, 0);
		}
		revalidate();
	}

}
