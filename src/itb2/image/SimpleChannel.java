package itb2.image;

import java.awt.Dimension;
import java.util.Iterator;

public class SimpleChannel implements Channel {
	private final Image image;
	private final int channel;
	
	public SimpleChannel(Image image, int channel) {
		this.image = image;
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
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}
	
	@Override
	public Dimension getSize() {
		return image.getSize();
	}

	@Override
	public double getValue(int column, int row) {
		return image.getValue(column, row, channel);
	}

	@Override
	public void setValue(int column, int row, double value) {
		image.setValue(column, row, channel, value);
	}

	@Override
	public Row getRow(int row) {
		return new SimpleRow(image, channel, row);
	}

	@Override
	public Column getColumn(int column) {
		return new SimpleColumn(image, channel, column);
	}

	@Override
	public Iterable<Row> rows() {
		return new Iterable<Row>() {
			@Override
			public Iterator<Row> iterator() {
				return new Iterator<Row>() {
					int row = 0;
					
					@Override
					public boolean hasNext() {
						return row < image.getHeight();
					}
					
					@Override
					public Row next() {
						return getRow(row++);
					}
				};
			}
		};
	}

	@Override
	public Iterable<Column> columns() {
		return new Iterable<Column>() {
			@Override
			public Iterator<Column> iterator() {
				return new Iterator<Column>() {
					int column = 0;
					
					@Override
					public boolean hasNext() {
						return column < image.getWidth();
					}
					
					@Override
					public Column next() {
						return getColumn(column++);
					}
				};
			}
		};
	}

}
