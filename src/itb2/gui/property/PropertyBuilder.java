package itb2.gui.property;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import itb2.filter.property.BooleanProperty;
import itb2.filter.property.DoubleProperty;
import itb2.filter.property.FilterProperty;
import itb2.filter.property.IntegerProperty;
import itb2.filter.property.OptionProperty;
import itb2.filter.property.RangeProperty;
import itb2.filter.property.StringProperty;

public abstract class PropertyBuilder {
	private static final Map<Class<? extends FilterProperty>, PropertyBuilder> builders;
	protected static final Font TITLE_FONT, VALUE_FONT;
	protected static final int SPACING;
	
	static {
		builders = new HashMap<>();
		TITLE_FONT = new Font(Font.DIALOG, Font.BOLD, 12);
		VALUE_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
		SPACING = 5;
		
		builders.put(BooleanProperty.class, new BooleanPropertyBuilder());
		builders.put(DoubleProperty.class, new DoublePropertyBuilder());
		builders.put(IntegerProperty.class, new IntegerPropertyBuilder());
		builders.put(OptionProperty.class, new OptionPropertyBuilder());
		builders.put(RangeProperty.class, new RangePropertyBuilder());
		builders.put(StringProperty.class, new StringPropertyBuilder());
	}
	
	public static void buildProperty(FilterProperty property, JPanel panel) {
		PropertyBuilder builder = builders.get(property.getClass());
		if(builder == null)
			throw new RuntimeException("Unknown FilterProperty: " + property.getClass().getSimpleName());
		builder.build(property, panel);
	}
	
	PropertyBuilder() {}
	
	public abstract void build(FilterProperty property, JPanel panel);
	
	protected JComponent getTitel(FilterProperty property) {
		JLabel title = new JLabel(property.name);
		title.setFont(TITLE_FONT);
		return title;
	}

}
