package itb2.image;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Image {
	
	public int getWidth();
	
	public int getHeight();
	
	public int getChannelCount();
	
	public double[][][] getData();
	
	public List<Selection> getSelections();
	
	public String getFileName();
	
	public BufferedImage asBufferedImage();

}
