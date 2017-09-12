package itb2.image;

import java.awt.Dimension;

public class HsiImage extends AbstractImage {
	public static final double MAX_HUE = 360, MAX_SATURATION = 100, MAX_INTENSITY = 255;
	public static final int HUE = 0, SATURATION = 1, INTENSITY = 2;
	
	public HsiImage(int width, int height) {
		super(width, height, 3);
	}
	
	public HsiImage(Dimension size) {
		super(size, 3);
	}
	
	public HsiImage(double[][][] data) {
		super(data);
		
		if(channelCount != 3)
			throw new RuntimeException("data must have three channels (hue , saturation and intensity)");
	}
	
	@Override
	protected double[] getRGB(int x, int y) {
		double h = data[x][y][HUE];
		double s = data[x][y][SATURATION];
		double i = data[x][y][INTENSITY];
		
		return hsi2rgb(h, s, i);
	}
	
	private double[] hsi2rgb(double h, double s, double i) {
		// normalisierte HSI-Werte:
		h *= 2 * Math.PI / MAX_HUE;
		s /= MAX_SATURATION;
		i /= MAX_INTENSITY;
		double x = i * (1 - s);
		double y = i * (1 + ((s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h))));
		double z = 3 * i - (x + y);
		
		double rgb[] = new double[3];
		if (h < (Math.PI * 2 / 3)) {
			rgb[2] = x;
			rgb[0] = y;
			rgb[1] = z;
		} else {
			if (h < 4 * Math.PI / 3) {
				h = h - 2 * Math.PI / 3;
				y = i * (1 + ((s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h))));
				z = 3 * i - (x + y);
				rgb[0] = x;
				rgb[1] = y;
				rgb[2] = z;
			} else {
				h = h - 4 * Math.PI / 3;
				y = i * (1 + ((s * Math.cos(h)) / (Math.cos(Math.PI / 3 - h))));
				z = 3 * i - (x + y);
				rgb[1] = x;
				rgb[2] = y;
				rgb[0] = z;
			}
		}

		// normalisierte Werte umwandeln und runden
		rgb[0] = Math.round(rgb[0] * 255);
		rgb[1] = Math.round(rgb[1] * 255);
		rgb[2] = Math.round(rgb[2] * 255);
		return rgb;
	}

}
