package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import itb2.filter.FilterProperty;

class IntegerPropertyBuilder extends PropertyBuilder {

	@Override
	public void build(FilterProperty property, JPanel panel) {
		JTextField value = new JTextField(Integer.toString((int)property.getValue()));
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
					int val = value.getText().matches("^-?$") ? 0 : Integer.parseInt(value.getText());
					property.setValue(val);
					oldVal = value.getText();
				} catch(NumberFormatException e) {
					SwingUtilities.invokeLater(() -> value.setText(oldVal));
				}
			}
		});
		
		panel.add(getTitel(property));
		panel.add(value);
	}

}
