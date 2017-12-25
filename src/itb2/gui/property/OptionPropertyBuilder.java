package itb2.gui.property;

import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import itb2.filter.FilterProperty;
import itb2.filter.FilterProperty.Option;

class OptionPropertyBuilder extends PropertyBuilder {

	@Override
	public void build(FilterProperty property, JPanel panel) {
		Option option = (Option)property.getValue();
		
		JComboBox<Object> value = new JComboBox<>(option.getOptions());
		value.setFont(VALUE_FONT);
		value.setSelectedItem(option.getSelection());
		value.addItemListener(e -> {
			if(e.getStateChange() == ItemEvent.SELECTED)
				option.setSelection(e.getItem());
		});
		
		panel.add(getTitel(property));
		panel.add(value);
	}

}
