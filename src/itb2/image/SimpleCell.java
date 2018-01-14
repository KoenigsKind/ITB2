package itb2.image;

/**
 * Represents the cell of a {@link Row} or a {@link Column}
 * 
 * @author Micha Strauch
 */
class SimpleCell implements Cell {

	/** Image this cell belongs to */
	private final Image image;

	/** Column, row and channel ID */
	private final int column, row, channel;

	/**
	 * Creates a cell accessing the given image with the given column, row and channel id.
	 * 
	 * @param image   Image this cell belongs to
	 * @param column  Column ID of this cell
	 * @param row     Row ID of this cell
	 * @param channel Channel ID of this cell
	 */
	public SimpleCell(Image image, int column, int row, int channel) {
		this.image = image;
		this.column = column;
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
	public int getColumnID() {
		return column;
	}

	@Override
	public double getValue() {
		return image.getValue(column, row, channel);
	}

	@Override
	public void setValue(double value) {
		image.setValue(column, row, channel, value);
	}

}
