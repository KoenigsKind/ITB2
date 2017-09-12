package itb2.image;

public interface Column extends Iterable<Cell> {
	
	public Image getImage();
	
	public int getChannelID();
	
	public int getColumnID();
	
	public int getSize();
	
	public double getValue(int row);
	
	public void setValue(int row, double value);
	
	public Cell getCell(int row);

}
