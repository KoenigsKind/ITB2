package itb2.image;

import java.util.Iterator;

/**
 * Represents the column of a {@link Channel}
 * 
 * @author Micha Strauch
 */
class SimpleColumn implements Column {

	/** Image this column belongs to */
	private final Image image;

	/** Column and channel ID */
	private final int column, channel;

	/**
	 * Creates a column accessing the given image with the given column and channel id.
	 * 
	 * @param image   Image this column belongs to
	 * @param column  Column ID of this column
	 * @param channel Channel ID of this column
	 */
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
