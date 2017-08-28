package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import itb2.filter.property.FilterProperty;
import itb2.filter.property.StringProperty;

class StringPropertyBuilder extends PropertyBuilder {

	@Override
	public JPanel build(FilterProperty property) {
		StringProperty stringProperty = (StringProperty) property;
		
		JTextField value = new JTextField(stringProperty.value);
		value.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				update();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				update();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				update();
			}
			
			private void update() {
				stringProperty.value = value.getText();
			}
		});
		
		return build(property, value);
	}

}
