package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import itb2.filter.property.FilterProperty;
import itb2.filter.property.IntegerProperty;

class IntegerPropertyBuilder extends PropertyBuilder {

	@Override
	public void build(FilterProperty property, JPanel panel) {
		IntegerProperty integerProperty = (IntegerProperty) property;
		
		JTextField value = new JTextField(Integer.toString(integerProperty.value));
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
				integerProperty.value = Integer.parseInt(value.getText());
			}
		});
		
		panel.add(getTitel(property));
		panel.add(value);
	}

}
