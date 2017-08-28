package itb2.gui.property;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import itb2.filter.property.BooleanProperty;
import itb2.filter.property.DoubleProperty;
import itb2.filter.property.FilterProperty;
import itb2.filter.property.IntegerProperty;
import itb2.filter.property.OptionProperty;
import itb2.filter.property.RangeProperty;
import itb2.filter.property.StringProperty;

public abstract class PropertyBuilder {
	private static final Map<Class<? extends FilterProperty>, PropertyBuilder> builders;
	protected static final Font TITLE_FONT;
	protected static final int SPACING;
	
	static {
		builders = new HashMap<>();
		TITLE_FONT = new Font(Font.DIALOG, Font.BOLD, 12);
		SPACING = 5;
		
		builders.put(BooleanProperty.class, new BooleanPropertyBuilder());
		builders.put(DoubleProperty.class, new DoublePropertyBuilder());
		builders.put(IntegerProperty.class, new IntegerPropertyBuilder());
		builders.put(OptionProperty.class, new OptionPropertyBuilder());
		builders.put(RangeProperty.class, new RangePropertyBuilder());
		builders.put(StringProperty.class, new StringPropertyBuilder());
	}
	
	public static JPanel buildProperty(FilterProperty property) {
		PropertyBuilder builder = builders.get(property.getClass());
		if(builder == null)
			throw new RuntimeException("Unknown FilterProperty: " + property.getClass().getSimpleName());
		return builder.build(property);
	}
	
	PropertyBuilder() {}
	
	public abstract JPanel build(FilterProperty property);
	
	protected JPanel build(FilterProperty property, JComponent value) {
		JLabel title = new JLabel(property.name);
		title.setFont(TITLE_FONT);
		
		SpringLayout layout = new SpringLayout();
		JPanel panel = new JPanel(layout);
		panel.add(title);
		panel.add(value);
		
		layout.putConstraint(SpringLayout.NORTH, title, SPACING, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.WEST, title, SPACING, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.EAST, title, 0, SpringLayout.EAST, value);
		layout.putConstraint(SpringLayout.NORTH, value, SPACING, SpringLayout.SOUTH, title);
		layout.putConstraint(SpringLayout.WEST, value, 2 * SPACING, SpringLayout.WEST, panel);
		layout.putConstraint(SpringLayout.SOUTH, panel, SPACING, SpringLayout.SOUTH, value);
		layout.putConstraint(SpringLayout.EAST, panel, new Spring() {
			int val;
			
			@Override
			public void setValue(int value) {
				this.val = value;
			}
			
			@Override
			public int getValue() {
				return val;
			}
			
			@Override
			public int getPreferredValue() {
				double titleWidth = title.getPreferredSize().getWidth();
				double valueWidth = value.getPreferredSize().getWidth();
				return SPACING + (int)(titleWidth > valueWidth ? 0 : valueWidth - titleWidth);
			}
			
			@Override
			public int getMinimumValue() {
				double titleWidth = title.getMinimumSize().getWidth();
				double valueWidth = value.getMinimumSize().getWidth();
				return SPACING + (int)(titleWidth > valueWidth ? 0 : valueWidth - titleWidth);
			}
			
			@Override
			public int getMaximumValue() {
				double titleWidth = title.getMaximumSize().getWidth();
				double valueWidth = value.getMaximumSize().getWidth();
				return SPACING + (int)(titleWidth > valueWidth ? 0 : valueWidth - titleWidth);
			}
		}, SpringLayout.EAST, title);
		
		return panel;
	}

}
