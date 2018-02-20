package itb2.engine;

import java.io.File;

/**
 * Parses the arguments passed to
 * the program when starting it.
 *
 * @author Micha Strauch
 */
public class ArgumentParser {
	
	/** Whether the {@link ConversionHelper} should be used. */
	private boolean enableHelper = true;
	
	/** File containing saved state, like opened filters and images. */
	private File config = new File("ITB2.bin");
	
	/** ArgumentParser should only be created via {@link #parse(String[])}. */
	private ArgumentParser() {}
	
	/**
	 * Parses the given arguments and either returns an instance of ArgumentParser
	 * containing the parsed arguments, or returns null to indicate the program
	 * should silently quit.
	 *  
	 * @param args Arguments to parse
	 * 
	 * @return ArgumentParser with parsed arguments or null
	 */
	public static ArgumentParser parse(String[] args) {
		ArgumentParser parser = new ArgumentParser();
		
		for(int i = 0; i < args.length; i++) {
			switch(args[i].toLowerCase()) {
				case "-conversionhelper":
				case "-ch":
					parser.enableHelper = Boolean.parseBoolean(args[++i]);
					break;
				case "-config":
				case "-c":
					if("null".equals(args[++i]))
						parser.config = null;
					else
						parser.config = new File(args[i]);
					break;
				case "-help":
				case "-h":
				default:
					System.out.println(parser.getHelp());
					return null;
			}
		}
		
		return parser;
	}
	
	/**
	 * Returns file containing saved state, like opened filters
	 * and images, or null if state should not be saved.
	 * 
	 * @return File containing saved state
	 */
	public File getConfigFile() {
		return config;
	}
	
	/** Whether the {@link ConversionHelper} should be used. */
	public boolean useConversionHelper() {
		return enableHelper;
	}
	
	/** Returns a help message, containing all optional arguments. */
	public String getHelp() {
		return "Optional arguments:\n"
				+ "\n"
				+ " -ConversionHelper <true|false> :: Enables/Disables automatic image conversion\n"
				+ " -ch <true|false>               :: Default: true\n"
				+ "\n"
				+ " -config <File> :: Uses the given File for storing the program state\n"
				+ " -c <File>      :: Use \"-config null\" to disable the config\n"
				+ "                :: Default \"" + config.getPath() + "\"\n"
				+ "\n"
				+ " -help :: Shows this help\n"
				+ " -h";
	}
	
}
