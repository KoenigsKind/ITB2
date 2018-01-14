package itb2.image;

import java.util.Iterator;

/**
 * Represents the row of a {@link Channel}
 * 
 * @author Micha Strauch
 */
class SimpleRow implements Row {

	/** Image this row belongs to */
	private final Image image;

	/** Row and channel ID */
	private final int row, channel;

	/**
	 * Creates a row accessing the given image with the given row and channel id.
	 * 
	 * @param image   Image this row belongs to
	 * @param row     Row ID of this row
	 * @param channel Channel ID of this row
	 */
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
