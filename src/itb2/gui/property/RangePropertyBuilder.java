package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import itb2.filter.property.FilterProperty;
import itb2.filter.property.RangeProperty;

class RangePropertyBuilder extends PropertyBuilder {

	@Override
	public JPanel build(FilterProperty property) {
		RangeProperty rangeProperty = (RangeProperty) property;
		
		JScrollBar value = new JScrollBar(JScrollBar.HORIZONTAL);
		value.setValue(rangeProperty.value);
		value.setMinimum(rangeProperty.min);
		value.setMaximum(rangeProperty.max);
		value.getModel().addChangeListener(e -> rangeProperty.value = value.getValue());
		
		return build(property, value);
	}

}
