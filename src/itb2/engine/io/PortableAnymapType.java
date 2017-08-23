package itb2.engine.io;

public enum PortableAnymapType {
	
	BITMAP_ASCII("P1", 1, false, false),
	BITMAP_BINARY("P4", 1, false, true),
	
	GRAYMAP_ASCII("P2", 1, true, false),
	GRAYMAP_BINARY("P5", 1, true, true),
	
	PIXMAP_ASCII("P3", 3, true, false),
	PIXMAP_BINARY("P6", 3, true, true);
	
	public final String magic;
	public final int channelCount;
	public final boolean hasMaxValue;
	public final boolean isBinary;
	
	private PortableAnymapType(String magic, int channelCount, boolean hasMaxValue, boolean isBinary) {
		this.magic = magic;
		this.hasMaxValue = hasMaxValue;
		this.channelCount = channelCount;
		this.isBinary = isBinary;
	}
	
	public static PortableAnymapType fromMagic(String magic) {
		for(PortableAnymapType type : values())
			if(type.magic.equals(magic))
				return type;
		return null;
	}

}
