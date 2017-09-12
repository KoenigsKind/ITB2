package itb2.image;

import java.awt.Dimension;

public interface Channel {
	
	public Image getImage();
	
	public int getChannelID();
	
	public int getWidth();
	
	public int getHeight();
	
	public Dimension getSize();
	
	public double getValue(int row, int column);
	
	public void setValue(int row, int column, double value);
	
	public Row getRow(int row);
	
	public Column getColumn(int column);
	
	public Iterable<Row> rows();
	
	public Iterable<Column> columns();
	
}
