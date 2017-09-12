package itb2.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

public interface Image {
	
	public int getWidth();
	
	public int getHeight();
	
	public Dimension getSize();
	
	public int getChannelCount();
	
	
	
	public double[] getValue(int x, int y);
	
	public double getValue(int x, int y, int channel);
	
	public void setValue(int x, int y, double... values);
	
	public void setValue(int x, int y, int channel, double value);
	
	
	
	public Channel getChannel(int channel);
	
	public List<Point> getSelections();
	
	public Object getName();
	
	public void setName(Object name);
	
	
	
	public BufferedImage asBufferedImage(); 

}
