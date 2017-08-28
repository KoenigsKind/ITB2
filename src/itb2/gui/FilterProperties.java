package itb2.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
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
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}
	
	private void filterChanged(ListSelectionEvent e, FilterList list) {
		if(e.getValueIsAdjusting())
			return;
		
		Filter filter = list.getSelectedFilter();
		
		ArrayList<FilterProperty> properties = new ArrayList<>();
		properties.addAll(filter.getProperties().getProperties());
		properties.sort((a, b) -> a.index - b.index);
		buildPanel(properties);
	}
	
	public void buildPanel(List<FilterProperty> properties) {
		removeAll();
		for(FilterProperty property : properties) {
			PropertyBuilder.buildProperty(property, this);
			add(Box.createVerticalStrut(5));
		}
		revalidate();
	}
	
	@Override
	public Component add(Component comp) {
		if(comp instanceof JComponent)
			((JComponent) comp).setAlignmentX(0);
		
		super.add(comp, 0.0);
		return comp;
	}

}
