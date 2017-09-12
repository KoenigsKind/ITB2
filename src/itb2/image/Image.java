package itb2.image;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

public interface Image extends Iterable<Channel> {
	
	public int getWidth();
	
	public int getHeight();
	
	public Dimension getSize();
	
	public int getChannelCount();
	
	
	
	public double[] getValue(int row, int column);
	
	public double getValue(int row, int column, int channel);
	
	public void setValue(int row, int column, double... values);
	
	public void setValue(int row, int column, int channel, double value);
	
	
	
	public Channel getChannel(int channel);
	
	public List<Point> getSelections();
	
	public Object getName();
	
	public void setName(Object name);
	
	
	
	public BufferedImage asBufferedImage(); 

}
