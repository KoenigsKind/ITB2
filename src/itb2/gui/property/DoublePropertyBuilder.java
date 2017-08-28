package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import itb2.filter.property.DoubleProperty;
import itb2.filter.property.FilterProperty;

class DoublePropertyBuilder extends PropertyBuilder {

	@Override
	public JPanel build(FilterProperty property) {
		DoubleProperty doubleProperty = (DoubleProperty) property;
		
		JTextField value = new JTextField(Double.toString(doubleProperty.value));
		//TODO https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
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
				doubleProperty.value = Double.parseDouble(value.getText());
			}
		});
		
		return build(property, value);
	}

}
