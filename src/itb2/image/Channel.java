package itb2.image;

public interface Channel {
	
	public Image getImage();
	
	public int getChannelID();
	
	public int getWidth();
	
	public int getHeight();
	
	public double getValue(int x, int y);
	
	public void setValue(int x, int y, double value);
	
}
