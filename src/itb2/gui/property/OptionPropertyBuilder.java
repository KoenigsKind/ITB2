package itb2.gui.property;

import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import itb2.filter.property.FilterProperty;
import itb2.filter.property.OptionProperty;

class OptionPropertyBuilder extends PropertyBuilder {

	@Override
	public JPanel build(FilterProperty property) {
		OptionProperty optionProperty = (OptionProperty) property;
		
		JComboBox<Object> value = new JComboBox<>(optionProperty.options);
		value.setSelectedItem(optionProperty.value);
		value.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED)
				optionProperty.value = e.getItem();
		});
		
		return build(property, value);
	}

}
