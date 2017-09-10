package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import itb2.filter.property.DoubleProperty;
import itb2.filter.property.FilterProperty;

class DoublePropertyBuilder extends PropertyBuilder {

	@Override
	public void build(FilterProperty property, JPanel panel) {
		DoubleProperty doubleProperty = (DoubleProperty) property;
		
		JTextField value = new JTextField(Double.toString(doubleProperty.value));
		value.setFont(VALUE_FONT);
		//TODO Allow only [-0-9.] https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
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
		
		panel.add(getTitel(doubleProperty));
		panel.add(value);
	}

}
