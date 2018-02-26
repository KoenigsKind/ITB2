package itb2.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.TreeMap;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

import itb2.image.Channel;
import itb2.image.Image;

/**
 * Panel to display the histogram of a given image.
 *
 * @author Micha Strauch
 */
public class Histogram extends JPanel {
	private static final long serialVersionUID = 2604981586246272368L;
	
	/** Whether the histogram should be cumulative or distinctive. */
	private static final String CUMULATIVE = "Cumulative", DISTINCTIVE = "Distinctive";
	
	/** Combobox for selecting the channel. */
	private final JComboBox<ChannelName> channel;
	
	/** Combobox for selecting the type of histogram. */
	private final JComboBox<String> method;
	
	/** Image to display histogram of. */
	private Image image;
	
	/** Constructor for a panel displaying the histogram of an image. */
	public Histogram() {
		Painter painter = new Painter();
		
		// Channel selection
		JLabel channelLabel = new JLabel("Channel:");
		channel = new JComboBox<>();
		channel.addItemListener(e -> painter.update());
		
		// Histogram type selection
		JLabel methodLabel = new JLabel("Method:");
		method = new JComboBox<>(new String[] {DISTINCTIVE, CUMULATIVE});
		method.addItemListener(e -> painter.update());
		
		// Build layout
		GroupLayout layout = new GroupLayout(this);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.CENTER, false)
						.addComponent(channelLabel)
						.addComponent(channel)
						.addComponent(methodLabel)
						.addComponent(method))
				.addGap(10)
				.addComponent(painter)
				.addGap(20));
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addGroup(Alignment.CENTER, layout.createSequentialGroup()
						.addGap(5)
						.addComponent(channelLabel)
						.addGap(5)
						.addComponent(channel, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(20)
						.addComponent(methodLabel)
						.addGap(5)
						.addComponent(method, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGap(5))
				.addGroup(layout.createSequentialGroup()
						.addGap(20)
						.addComponent(painter)
						.addGap(20)));
		
		// Add elements to this panel
		add(channelLabel);
		add(channel);
		add(methodLabel);
		add(method);
		add(painter);
		setLayout(layout);
	}
	
	/**
	 * Sets the image.
	 * 
	 * @param image Image, to show histogram of. 
	 */
	public void setImage(Image image) {
		this.image = image;
		channel.removeAllItems();
		for(ChannelName name : ChannelName.forImage(image))
			channel.addItem(name);
	}
	
	/**
	 * Panel that actually contains the histogram.
	 *
	 * @author Micha Strauch
	 */
	private class Painter extends JPanel {
		private static final long serialVersionUID = 6004160219508478853L;
		
		/** Possible value type:<br>Binary (0 & 1), Integer (0 - 255), Rational */
		private static final int BINARY = 2, INTEGER = 256, RATIONAL = 0;
		
		/** Mapping number of pixels to each existing pixel value. */
		private final TreeMap<Double, Integer> values;
		
		/** Last rendered image */
		private Image lastImage;
		
		/** Last rendered channel */
		private ChannelName lastChannel;
		
		/** Minimum and maximum pixel value. */
		private double min, max;
		
		/** Of what type the values are.<p>{@link #BINARY} / {@link #INTEGER} / {@link #RATIONAL} */
		private int valueType;
		
		/** Array containing currently displayed histogram data. */
		private int[] data;
		
		/** Maximum value in {@link #data}. */
		private int maxData;
		
		/** Constructor for this histogram painter. */
		Painter() {
			values = new TreeMap<>();
			
			// Register this JPanel for displaying tooltips
			ToolTipManager.sharedInstance().registerComponent(this);
		}
		
		/** Updates this histogram if any inputs changed. */
		public void update() {
			
			// Skip recreating values, if nothing has changed
			if(image != lastImage || lastChannel != channel.getSelectedItem()) {
				lastImage = image;
				lastChannel = (ChannelName) channel.getSelectedItem();
				
				// Build values
				min = Double.MAX_VALUE;
				max = Double.MIN_VALUE;
				valueType = BINARY;
				values.clear();
				
				if(lastImage != null) {
					Channel chan = lastImage.getChannel(lastChannel.getChannel());
					for(int col = 0; col < chan.getWidth(); col++) {
						for(int row = 0; row < chan.getHeight(); row++) {
							double value = chan.getValue(col, row);
							min = value < min ? value : min;
							max = value > max ? value : max;
							
							Integer count = values.get(value);
							if(count != null)
								values.put(value, count + 1);
							else {
								values.put(value, 1);
								
								// Check if value matches the current value type
								if(valueType != RATIONAL && (value < 0 || value > 255 || value != (int) value))
									valueType = RATIONAL;
								if(valueType == BINARY && value != 0 && value != 1)
									valueType = INTEGER;
							}
						}
					}
				}
			}
			
			// Repaint the histogram
			repaint();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Skip if nothing to display
			if(values.isEmpty())
				return;
			
			// Choose type of histogram
			boolean cumulative = CUMULATIVE == method.getSelectedItem();
			
			// Width of each bar
			int barCount = valueType == RATIONAL ? getWidth() : valueType;
			int barWidth = getWidth() / barCount;
			
			// Value range each bar contains
			double step = valueType == RATIONAL ? (max - min) / barCount : 1;
			data = new int[getWidth()];
			maxData = Integer.MIN_VALUE;
			
			// Set border, so histogram stays in center
			int deltaX = (getWidth() - barCount * barWidth) / 2;
			
			// Calculate bar heights and fill data array
			int previous = 0;
			for(int i = 0; i < barCount; i++) {
				int value = previous + getValue(i * step, (i + 1) * step);
				
				if(cumulative)
					previous = value;
				
				maxData = value > maxData ? value : maxData;
				
				for(int x = 0; x < barWidth; x++)
					data[i * barWidth + x + deltaX] = value;
			}
			
			// Draw background
			g.setColor(Color.WHITE);
			g.fillRect(deltaX, 0, barWidth * barCount, getHeight());
			
			// Draw histogram
			g.setColor(Color.BLACK);
			step = (double) getHeight() / maxData;
			for(int x = 0; x < data.length; x++) {
				int y = (int)(data[x] * step);
				g.drawLine(x, getHeight() - y, x, getHeight());
			}
		}
		
		/**
		 * Returns the number of pixels which have a value
		 * between min (including) and max (excluding).
		 * <p>
		 * Exception: If max == {@link #max max value}
		 *            it's included.
		 * 
		 * @param min Minimum value (including)
		 * @param max Maximum value (excluding)
		 * 
		 * @return Number of pixels with value in given range.
		 */
		private int getValue(double min, double max) {
			return values.subMap(min, true, max, max == this.max)
					.values()
					.stream()
					.mapToInt(i -> i.intValue())
					.sum();
		}
		
		@Override
		public String getToolTipText(MouseEvent event) {
			int x = event.getX();
			if(x < 0 || data == null || x >= data.length)
				return "--";
			
			// Make sure all tooltips have the same length
			String maxD = String.format("%,d", maxData);
			String d = String.format("%,d", data[x]);
			for(int i = d.length(); i < maxD.length(); i++)
				d = "&nbsp;" + d;
			
			// Return tooltip with monospace font
			// This way digits and space have the same width
			return "<html><code>" + d + "</code></html>";
		}
		
		@Override
		public Point getToolTipLocation(MouseEvent event) {
			// Show tooltip at the top of the current bar
			double step = (double) getHeight() / maxData;
			int x = event.getX();
			int y = (int)(data[x] * step);
			return new Point(x, getHeight() - y);
		}
		
	}
	
}
