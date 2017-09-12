package itb2.image;

public interface Row extends Iterable<Cell> {
	
	public Image getImage();
	
	public int getChannelID();
	
	public int getRowID();
	
	public int getSize();
	
	public double getValue(int column);
	
	public void setValue(int column, double value);
	
	public Cell getCell(int column);

}
