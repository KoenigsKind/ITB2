package itb2.image;

import java.util.Iterator;

class SimpleColumn implements Column {
	private final Image image;
	private final int channel;
	private final int column;
	
	public SimpleColumn(Image image, int channel, int column) {
		this.image = image;
		this.channel = channel;
		this.column = column;
	}

	@Override
	public Image getImage() {
		return image;
	}

	@Override
	public int getChannelID() {
		return channel;
	}

	@Override
	public int getColumnID() {
		return column;
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public double getValue(int row) {
		return image.getValue(row, column, channel);
	}

	@Override
	public void setValue(int row, double value) {
		image.setValue(row, column, channel, value);
	}

	@Override
	public Cell getCell(int row) {
		return new SimpleCell(image, row, column, channel);
	}

	@Override
	public Iterator<Cell> iterator() {
		return new Iterator<Cell>() {
			int row = 0;
			
			@Override
			public boolean hasNext() {
				return row < image.getHeight();
			}
			
			@Override
			public Cell next() {
				return getCell(row++);
			}
		};
	}

}
