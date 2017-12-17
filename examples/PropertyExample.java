import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import itb2.filter.AbstractFilter;
import itb2.image.DrawableImage;
import itb2.image.Image;

/**
 * Exemplary filter showing all property types
 * 
 * @author Micha Strauch
 */
public class PropertyExample extends AbstractFilter {
	
	/** Property names */
	private static final String BOOLEAN = "Boolean Property", DOUBLE = "Double Property", INTEGER = "Integer Property",
			OPTION = "Option Property", RANGE = "Range Property", STRING = "String Property";
	
	/** Constructor, setting the default values for the properties */
	public PropertyExample() {
		getProperties().addBooleanProperty(BOOLEAN, true);
		getProperties().addDoubleProperty(DOUBLE, Math.PI);
		getProperties().addIntegerProperty(INTEGER, 1337);
		getProperties().addOptionProperty(OPTION, "Charlie", "Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot");
		getProperties().addRangeProperty(RANGE, 42, 0, 7, 98);
		getProperties().addStringProperty(STRING, "Hello World!");
	}
	
	@Override
	public Image[] filter(Image[] input) {
		DrawableImage image = new DrawableImage(300, 114);
		image.setName(getClass().getSimpleName());
		Graphics g = image.getGraphics();
		g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
		g.setColor(Color.WHITE);
		
		g.drawString(String.format("%s: %b",   BOOLEAN, getProperties().getBooleanProperty(BOOLEAN)), 14,  25);
		g.drawString(String.format("%s:  %f",  DOUBLE,  getProperties().getDoubleProperty(DOUBLE)),   14,  40);
		g.drawString(String.format("%s: %d",   INTEGER, getProperties().getIntegerProperty(INTEGER)), 14,  55);
		g.drawString(String.format("%s:  %s",  OPTION,  getProperties().getOptionProperty(OPTION)),   14,  70);
		g.drawString(String.format("%s:   %d", RANGE,   getProperties().getRangeProperty(RANGE)),     14,  85);
		g.drawString(String.format("%s:  %s",  STRING,  getProperties().getStringProperty(STRING)),   14, 100);

		return new Image[]{image};
	}
	
}
