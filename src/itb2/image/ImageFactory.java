package itb2.image;

public final class ImageFactory {
	public static final Class<?> RGB = RgbImage.class, HSV = HsiImage.class, GRAYSCALE = GrayscaleImage.class;
	private ImageFactory() {}
	
	public static Image create(Class<?> type, double[][]... data) {
		if(type == RGB)
			return new RgbImage(data);
		if(type == HSV)
			return new HsiImage(data);
		if(type == GRAYSCALE)
			return new GrayscaleImage(data[0]);
		
		throw new RuntimeException("Unknown type: " + type.getName());
	}
	
	public static Image rgb(double[][]... data) {
		return create(RGB, data);
	}
	
	public static Image hsv(double[][]... data) {
		return create(HSV, data);
	}
	
	public static Image grayscale(double[][] data) {
		return create(GRAYSCALE, data);
	}

}
