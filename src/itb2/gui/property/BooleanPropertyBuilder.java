package itb2.gui.property;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import itb2.filter.property.BooleanProperty;
import itb2.filter.property.FilterProperty;

class BooleanPropertyBuilder extends PropertyBuilder {

	@Override
	public void build(FilterProperty property, JPanel panel) {
		BooleanProperty booleanProperty = (BooleanProperty) property;
		
		JCheckBox checkBox = new JCheckBox(booleanProperty.name, booleanProperty.value);
		checkBox.setFont(TITLE_FONT);
		checkBox.addChangeListener(e -> booleanProperty.value = checkBox.isSelected());
		
		panel.add(checkBox);
	}

}
