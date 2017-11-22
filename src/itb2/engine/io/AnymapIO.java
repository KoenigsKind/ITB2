package itb2.engine.io;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import itb2.image.Image;
import itb2.image.ImageFactory;

public class AnymapIO {
	private static final byte[] MAGIC_COMMENT = "P6\n# Created with ImageToolbox 2\n".getBytes(), SPACE = "\n".getBytes(), MAX_VALUE = "\n255\n".getBytes();
	private static final int MAGIC_VALUE = 0, WIDTH = 1, HEIGHT = 2, MAX = 3;
	
	public static void save(Image image, File file) throws IOException {
		try(BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
			
			BufferedImage buff = image.asBufferedImage();
			int width = buff.getWidth(), height = buff.getHeight();
			
			// --- HEADER ---
			output.write(MAGIC_COMMENT);
			output.write(Integer.toString(width).getBytes());
			output.write(SPACE);
			output.write(Integer.toString(height).getBytes());
			output.write(MAX_VALUE);
			
			// --- VALUES ---
			for(int row = 0; row < height; row++) {
				for(int col = 0; col < width; col++) {
					int val = buff.getRGB(col, row);
					
					output.write((val >>> 16) & 0xFF);
					output.write((val >>>  8) & 0xFF);
					output.write((val       ) & 0xFF);
				}
			}
			
			// --- FINAL ---
			output.flush();
		}
	}
	
	public static Image load(File file) throws IOException {
		try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
			int[] header = new int[4];
			
			if(input.read() != 'P')
				throw new IOException("Not a Portable Anymap");
			
			// --- HEADER ---
			boolean newline = true;
			int val = 0, mode = 0;
			while(true) {
				int b = input.read();
				if(b < 0)
					throw new IOException("Image file corrupted");
				
				// Skip comments
				if(newline && b == '#') {
					while((b = input.read()) != '\n')
						if(b < 0)
							throw new IOException("Image file corrupted");
					continue;
				}
				
				newline = b == '\n';
				
				// Handle whitespace
				if(Character.isWhitespace(b)) {
					header[mode] = val;
					val = 0;
					if(++mode > 3) // Graymap | Pixmap
						break;
					if(mode > 2 && (header[MAGIC_VALUE] == 1 || header[MAGIC_VALUE] == 4)) // Bitmap
						break;
					continue;
				}
				
				// Check if digit
				if(Character.isDigit(b)) {
					val *= 10;
					val += (b - '0');
				} else
					throw new IOException("Image file corrupted, unknown symbol: '" + (char)b + "'");
			}
			
			// --- PREPARATION ---
			if(header[MAGIC_VALUE] < 1 || header[MAGIC_VALUE] > 6)
				throw new IOException("Unknown magic value: P" + header[MAGIC_VALUE]);
			
			boolean ascii = header[MAGIC_VALUE] < 4;
			double factor = 255;
			if(header[MAGIC_VALUE] != 1 && header[MAGIC_VALUE] != 4) // Not a bitmap
				factor = 255. / header[MAX];
			
			ImageFactory factory = (factor == 255 || factor == 1)
					? ImageFactory.bytePrecision() : ImageFactory.doublePrecision(); 
			Image image;
			int channels;
			
			if(header[MAGIC_VALUE] == 3 || header[MAGIC_VALUE] == 6) { // Pixmap
				image = factory.rgb(header[WIDTH], header[HEIGHT]);
				channels = 3;
			} else { // Bitmap or graymap
				image = factory.gray(header[WIDTH], header[HEIGHT]);
				channels = 1;
			}
			
			// --- VALUES ---
			if(ascii) {
				int row = 0, col = 0, chan = 0;
				boolean digit = false;
				val = 0;
				
				while(true) {
					int b = input.read();
					if(b < 0) {
						if(digit && row + 1 == header[HEIGHT] && col + 1 == header[WIDTH] && chan + 1 == channels) {
							// Storing last digit
							double value = val * factor;
							image.setValue(row, col, chan, value);
							break;
						} else
							throw new IOException("EOF too early");
					}
					
					if(Character.isDigit(b)) {
						val *= 10;
						val += (b - '0');
						digit = true;
					} else if(Character.isWhitespace(b)) {
						if(digit) {
							digit = false;
							
							double value = val * factor;
							image.setValue(row, col, chan, value);
							
							val = 0;
							
							if(++chan >= channels) {
								chan = 0;
								if(++col >= header[WIDTH]) {
									col = 0;
									if(++row >= header[HEIGHT])
										break;
								}
							}
						}
					} else
						throw new IOException("Image file corrupted, unknown symbol: '" + (char)b + "'");
				}
			} else {
				for(int row = 0; row < header[HEIGHT]; row++) {
					for(int col = 0; col < header[WIDTH]; col++) {
						for(int chan = 0; chan < channels; chan++) {
							int b = input.read();
							if(b < 0)
								throw new IOException("EOF too early");
							
							double value = b * factor;
							image.setValue(row, col, chan, value);
						}
					}
				}
			}
			
			return image;
		}
	}

}
