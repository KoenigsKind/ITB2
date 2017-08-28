package itb2.gui.property;

import javax.swing.JPanel;
import javax.swing.JSlider;

import itb2.filter.property.FilterProperty;
import itb2.filter.property.RangeProperty;

class RangePropertyBuilder extends PropertyBuilder {

	@Override
	public void build(FilterProperty property, JPanel panel) {
		RangeProperty rangeProperty = (RangeProperty) property;
		
		int min = rangeProperty.min;
		int max = rangeProperty.max;
		if(max < min) {
			min ^= max;
			max ^= min;
			min ^= max;
		}
		
		int val = rangeProperty.value;
		if(val < min || max < val)
			val = min;
		
		int minorStep = rangeProperty.step;
		int majorStep = minorStep;
		while(4 * majorStep < max - min)
			majorStep += minorStep;
		
		JSlider value = new JSlider(JSlider.HORIZONTAL, min, max, val);
		value.setMinorTickSpacing(minorStep);
		value.setMajorTickSpacing(majorStep);
		value.setPaintTicks(true);
		value.setPaintLabels(true);
		value.setSnapToTicks(true);
		
		
		value.getModel().addChangeListener(e -> rangeProperty.value = value.getValue());
		
		panel.add(getTitel(property));
		panel.add(value);
	}

}
