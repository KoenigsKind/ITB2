package itb2.image;

import java.util.Iterator;

class SimpleRow implements Row {
	private final Image image;
	private final int row, channel;
	
	public SimpleRow(Image image, int row, int channel) {
		this.image = image;
		this.row = row;
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
	public int getRowID() {
		return row;
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public double getValue(int column) {
		return image.getValue(column, row, channel);
	}

	@Override
	public void setValue(int column, double value) {
		image.setValue(column, row, channel, value);
	}

	@Override
	public Cell getCell(int column) {
		return new SimpleCell(image, column, row, channel);
	}

	@Override
	public Iterator<Cell> iterator() {
		return new Iterator<Cell>() {
			int column = 0;
			
			@Override
			public boolean hasNext() {
				return column < image.getWidth();
			}
			
			@Override
			public Cell next() {
				return getCell(column++);
			}
		};
	}

}
