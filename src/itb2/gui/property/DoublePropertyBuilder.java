package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
		value.getDocument().addDocumentListener(new DocumentListener() {
			private String oldVal = value.getText();
			
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
				try {
					double val = value.getText().isEmpty() ? 0 : Double.parseDouble(value.getText().replace(',', '.'));
					doubleProperty.value = val;
					oldVal = value.getText();
				} catch(NumberFormatException e) {
					SwingUtilities.invokeLater(() -> value.setText(oldVal));
				}
			}
		});
		
		panel.add(getTitel(doubleProperty));
		panel.add(value);
	}

}
