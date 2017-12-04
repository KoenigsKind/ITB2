package itb2.image;

import java.util.Iterator;

class SimpleColumn implements Column {
	private final Image image;
	private final int column, channel;
	
	public SimpleColumn(Image image, int column, int channel) {
		this.image = image;
		this.column = column;
		this.channel = channel;
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
		return image.getValue(column, row, channel);
	}

	@Override
	public void setValue(int row, double value) {
		image.setValue(column, row, channel, value);
	}

	@Override
	public Cell getCell(int row) {
		return new SimpleCell(image, column, row, channel);
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
