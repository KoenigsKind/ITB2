package itb2.image;

public interface Cell {
	
	public Image getImage();
	
	public int getChannelID();
	
	public int getRowID();
	
	public int getColumnID();
	
	public double getValue();
	
	public void setValue(double value);

}
