package itb2.gui.property;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import itb2.filter.property.BooleanProperty;
import itb2.filter.property.FilterProperty;

class BooleanPropertyBuilder extends PropertyBuilder {

	@Override
	public JPanel build(FilterProperty property) {
		BooleanProperty booleanProperty = (BooleanProperty) property;
		
		JCheckBox checkBox = new JCheckBox(booleanProperty.name, booleanProperty.value);
		checkBox.addChangeListener(e -> booleanProperty.value = checkBox.isSelected());
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(checkBox);
		
		return panel;
	}

}
