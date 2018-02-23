package itb2.gui;

import itb2.image.BinaryImage;
import itb2.image.GrayscaleImage;
import itb2.image.GroupedImage;
import itb2.image.HsiImage;
import itb2.image.HsvImage;
import itb2.image.Image;
import itb2.image.RgbImage;

/**
 * Helper class for {@link Histogram histogram}: Creates a nice name for all known channel types.
 *
 * @author Micha Strauch
 */
public class ChannelName {
	
	/** Channels of default image types. */
	public static final ChannelName RED, GREEN, BLUE, HUE, SATURATION, VALUE, INTENSITY, GRAYSCALE, BINARY, GROUP;
	
	/** Name of the channel. */
	private final String name;
	
	/** Channel ID. */
	private final int channel;
	
	static {
		// RGB-Image
		RED = new ChannelName("Red", 0);
		GREEN = new ChannelName("Green", 1);
		BLUE = new ChannelName("Blue", 2);
		
		// HSV- / HSI-Image
		HUE = new ChannelName("Hue", 0);
		SATURATION = new ChannelName("Saturation", 1);
		VALUE = new ChannelName("Value", 2);
		INTENSITY = new ChannelName("Intensity", 2);
		
		// Images with one channel
		GRAYSCALE = new ChannelName("Grayscale", 0);
		BINARY = new ChannelName("Binary", 0);
		GROUP = new ChannelName("Group", 0);
	}
	
	/**
	 * Creates a channel with given name and channel ID.
	 * 
	 * @param name    Name of the channel
	 * @param channel ID of the channel
	 */
	public ChannelName(String name, int channel) {
		this.name = name;
		this.channel = channel;
	}
	
	/** Returns the channel ID. */
	public int getChannel() {
		return channel;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Returns an array with channel names for the given image.
	 * 
	 * @param image Image to get channel names for
	 * 
	 * @return Array with channel names
	 */
	public static ChannelName[] forImage(Image image) {
		
		// Known image types
		if(image instanceof RgbImage)
			return new ChannelName[] {RED, GREEN, BLUE};
		if(image instanceof HsvImage)
			return new ChannelName[] {HUE, SATURATION, VALUE};
		if(image instanceof HsiImage)
			return new ChannelName[] {HUE, SATURATION, INTENSITY};
		if(image instanceof GrayscaleImage)
			return new ChannelName[] {GRAYSCALE};
		if(image instanceof GroupedImage)
			return new ChannelName[] {GROUP};
		if(image instanceof BinaryImage)
			return new ChannelName[] {BINARY};
		
		// Unknown image types
		ChannelName[] names = new ChannelName[image.getChannelCount()];
		for(int i = 0; i < names.length; i++)
			names[i] = new ChannelName("Channel " + (i + 1), i);
		return names;
	}
	
}
