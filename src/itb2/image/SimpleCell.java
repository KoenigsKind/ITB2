package itb2.image;

class SimpleCell implements Cell {
	private final Image image;
	private final int row, column, channel;
	
	public SimpleCell(Image image, int row, int column, int channel) {
		this.image = image;
		this.row = row;
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
	public int getRowID() {
		return row;
	}

	@Override
	public int getColumnID() {
		return column;
	}

	@Override
	public double getValue() {
		return image.getValue(row, column, channel);
	}

	@Override
	public void setValue(double value) {
		image.setValue(row, column, channel, value);
	}

}
