package itb2.engine.io;

import java.io.IOException;
import java.io.InputStream;

import itb2.image.Image;

public class PortableAnymapReader {
	private final InputStream input;
	
	public PortableAnymapReader(InputStream input) {
		this.input = input;
	}
	
	public Image read() throws IOException {
		Header header = readHeader();
		return header.type.isBinary ? readBinary() : readAscii();
	}
	
	private Header readHeader() throws IOException {
		Header header = new Header();
		boolean newLine = true;
		String value = "";
		int position = 0;
		
parsing:
		for(int next = input.read(); next >= 0; next = input.read()) {
			if(newLine && next == '#') {
				do {
					next = input.read();
				} while(next >= 0 && next != '\r' && next != '\n');
			} else if(next == '\r' || next == '\n' || next == '\t' || next == ' ') {
				if(!value.isEmpty()) {
					switch(position) {
						case 0:
							header.type = PortableAnymapType.fromMagic(value);
							break;
						case 1:
							header.width = Integer.valueOf(value);
							break;
						case 2:
							header.height = Integer.valueOf(value);
							if(!header.type.hasMaxValue)
								break parsing;
							break;
						case 3:
							header.maxValue = Integer.valueOf(value);
							break parsing;
					}
					
					value = "";
					position++;
					newLine = next == '\r' || next == '\n';
				}
			} else {
				value += (char)next;
			}
		}
		
		return header;
	}
	
	public Image readBinary() throws IOException {
		return null;
	}
	
	public Image readAscii() throws IOException {
		return null;
	}
	
	private static class Header {
		public PortableAnymapType type;
		public int width, height;
		public int maxValue;
	}

}
