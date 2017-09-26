package imageToolBox;

import java.awt.Frame;
import java.awt.Point;
import java.io.*;

import itb2.engine.io.ImageIO;
import itb2.filter.FilterProperties;
import itb2.image.Image;
import itb2.image.RgbImage;

/**
 * This class only exists for backwards compatibility and <u>should not be used!</u>
 * <p>
 * <strong>Use instead:</strong> {@link itb2.filter.AbstractFilter}
 */
@Deprecated
public class AbstractFilter {
	public final FilterProperties properties = new FilterProperties();
	protected int width;
	protected int height;
	
	public AbstractFilter() {
		initGUI();
	}

	public final boolean saveImage(double[][][] img, String filename) {
		Image image = new RgbImage(img);
		try {
			ImageIO.save(image, new File(filename));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public final void createDatafile() {
		createDatafile(null, true);
	}

	public final void createDatafile(String filename) {
		createDatafile(filename, true);
	}

	public final void createDatafile(String filename, boolean deleteOnExit) {
		throw new UnsupportedOperationException();
	}

	public final void write(String s) {
		throw new UnsupportedOperationException();
	}

	public final void writeLine(String s) {
		throw new UnsupportedOperationException();
	}

	public final String readLine() {
		throw new UnsupportedOperationException();
	}

	public final void close() {
		throw new UnsupportedOperationException();
	}

	public final File getDatafile() {
		throw new UnsupportedOperationException();
	}

	public final void setDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public final void setSrcData(File... files) {
		throw new UnsupportedOperationException();
	}

	public final String readSrcDataLine() {
		throw new UnsupportedOperationException();
	}

	public final String readSrcDataLine(int i) {
		throw new UnsupportedOperationException();
	}

	// ====== Helfer-Funktionen ======

	/**
	 * Konvertierung eines Farbbildes in Graustufenbild
	 * 
	 * [grauwert = 0.299*R + 0.5870*G + 0.1140*B]
	 */
	public final void toGreyscale(double[][][] src) {
		for (int i = 0; i < src[0].length; i++)
			for (int j = 0; j < src[0][0].length; j++) {
				double greyvalue = 0.299 * src[0][i][j] + 0.5870 * src[1][i][j] + 0.1140 * src[2][i][j];
				for (int b = 0; b < 3; b++)
					src[b][i][j] = greyvalue;
			}
	}

	public void filter(double[][][] src, double[][][] dst) {}

	public void filter(double[][][] src1, double[][][] src2, double[][][] dst) {}

	public boolean hasProperties() {
		return false;
	}

	public void handleMouseClick(Point p) {}

	public void initGUI() {}

	public final Frame getPropertiesFrame() {
		throw new UnsupportedOperationException();
	}

	public final void initPropertiesFrame() {}

	public final void addDoubleProperty(String name) {
		addDoubleProperty(name, 0.0);
	}

	public final void addDoubleProperty(String name, double value) {
		properties.addDoubleProperty(name, value);
	}

	public final double getDoubleProperty(String name) {
		return properties.getDoubleProperty(name);
	}

	public final void addBooleanProperty(String name) {
		addBooleanProperty(name, false);
	}

	public final void addBooleanProperty(String name, boolean value) {
		properties.addBooleanProperty(name, value);
	}

	public final boolean getBooleanProperty(String name) {
		return properties.getBooleanProperty(name);
	}
}
