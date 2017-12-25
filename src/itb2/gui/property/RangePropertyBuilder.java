package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JSlider;

import itb2.filter.FilterProperty;
import itb2.filter.FilterProperty.Range;

class RangePropertyBuilder extends PropertyBuilder {

	@Override
	public void build(FilterProperty property, JPanel panel) {
		Range range = (Range)property.getValue();
		
		int min = range.getMin();
		int max = range.getMax();
		if(max < min) {
			min ^= max;
			max ^= min;
			min ^= max;
		}
		
		int val = range.getSelection();
		if(val < min || max < val)
			val = min;
		
		boolean preferOdd = Math.abs(max) == Math.abs(min); // If 0 in center, prefer odd number of labels 
		int minorStep = range.getStep();
		int minorStepCount = (max - min) / minorStep;
		int majorStep = minorStep;
		
		// Find best major step count
		if(minorStepCount % 4 == 0) // Try to use exactly 5 labels
			majorStep = (max - min) / 4;
		else if(!preferOdd && minorStepCount % 3 == 0) // Try to use exactly 4 labels
			majorStep = (max - min) / 3;
		else if(minorStepCount % 2 == 0) // Try to use exactly 3 labels
			majorStep = (max - min) / 2;
		else if(preferOdd && minorStepCount % 3 == 0) // Try to use exactly 4 labels
			majorStep = (max - min) / 3;
		else while(4 * majorStep < max - min) // Use best match with 4 labels
			majorStep += minorStep;
		
		JSlider value = new JSlider(JSlider.HORIZONTAL, min, max, val);
		value.setFont(VALUE_FONT);
		value.setMinorTickSpacing(minorStep);
		value.setMajorTickSpacing(majorStep);
		value.setPaintTicks(true);
		value.setPaintLabels(true);
		value.setSnapToTicks(true);
		
		
		value.getModel().addChangeListener(e -> range.setSelection(value.getValue()));
		
		panel.add(getTitel(property));
		panel.add(value);
	}

}
