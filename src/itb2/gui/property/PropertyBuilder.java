package itb2.gui.property;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import itb2.filter.FilterProperty;

public abstract class PropertyBuilder {
	private static final Map<Class<?>, PropertyBuilder> builders;
	protected static final Font TITLE_FONT, VALUE_FONT;
	protected static final int SPACING;
	
	static {
		builders = new HashMap<>();
		TITLE_FONT = new Font(Font.DIALOG, Font.BOLD, 12);
		VALUE_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
		SPACING = 5;
		
		builders.put(Boolean.class, new BooleanPropertyBuilder());
		builders.put(Double.class, new DoublePropertyBuilder());
		builders.put(Integer.class, new IntegerPropertyBuilder());
		builders.put(String.class, new StringPropertyBuilder());
		builders.put(FilterProperty.Option.class, new OptionPropertyBuilder());
		builders.put(FilterProperty.Range.class, new RangePropertyBuilder());
	}
	
	public static void buildProperty(FilterProperty<?> property, JPanel panel) {
		PropertyBuilder builder = builders.get(property.getClassOfT());
		if(builder == null)
			throw new RuntimeException("Unknown FilterProperty: " + property.getClassOfT().getSimpleName());
		builder.build(property, panel);
	}
	
	PropertyBuilder() {}
	
	public abstract void build(FilterProperty<?> property, JPanel panel);
	
	protected JComponent getTitel(FilterProperty<?> property) {
		JLabel title = new JLabel(property.getName());
		title.setFont(TITLE_FONT);
		return title;
	}

}
