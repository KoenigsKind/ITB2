package itb2.image;

class SimpleCell implements Cell {
	private final Image image;
	private final int column, row, channel;
	
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
