package itb2.gui.property;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import itb2.filter.FilterProperty;

class BooleanPropertyBuilder extends PropertyBuilder {

	@Override
	public void build(FilterProperty property, JPanel panel) {
		JCheckBox checkBox = new JCheckBox(property.getName(), (boolean)property.getValue());
		checkBox.setFont(TITLE_FONT);
		checkBox.addChangeListener(e -> property.setValue(checkBox.isSelected()));
		
		panel.add(checkBox);
	}

}
