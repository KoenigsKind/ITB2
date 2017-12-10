package itb2.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Conversion map
 * <p>
 * Map keeping track of how to get from one type of object to another type of object.
 * 
 * @author Micha Strauch
 */
public class PathMap<T> {
	
	protected final Class<T> classOfT;
	
	/** Indices for object types in map */
	protected final List<Class<? extends T>> order = new LinkedList<>();
	
	/**
	 * Map with conversion paths.
	 * <p>
	 * {@code map[src][dst]} contains the path to get from
	 * object type {@code src} to object type {@code dst}
	 */ 
	@SuppressWarnings("unchecked")
	protected Path<T>[][] map = new Path[10][10];
	
	public PathMap(Class<T> classOfT) {
		if(classOfT == null)
			throw new NullPointerException(); //Fail fast
		this.classOfT = classOfT;
	}
	
	/**
	 * Converts the given object into the requested object type 
	 * 
	 * @param o           Object to convert
	 * @param destination Requested object type
	 * 
	 * @return Converted object
	 * 
	 * @throws ConversionException If conversion unsuccessful
	 */
	public <R extends T> R convert(T o, Class<R> destination) throws ConversionException {
		
		// First check for exact conversion
		int convSrc = order.indexOf(o.getClass());
		int convDst = order.indexOf(destination);
		
		if(convSrc >= 0 && convDst >= 0)
			if(map[convSrc][convDst] != null && map[convSrc][convDst].length() == 1)
				return destination.cast(map[convSrc][convDst].convert(o));
		
		// Check for conversion using sub and super types
		List<Path<T>> paths = new ArrayList<>();
		for(int src = 0; src < order.size(); src++) {
			if( !order.get(src).isInstance(o) )
				continue;
			
			for(int dst = 0; dst < order.size(); dst++) {
				if( !destination.isAssignableFrom(order.get(dst)) )
					continue;
				
				if(map[src][dst] != null)
					paths.add(map[src][dst]);
			}
		}
		
		if(paths.isEmpty())
			throw new ConversionException("No conversion found");
		
		// Take path with fewest conversions
		paths.sort((a,b) -> a.length() - b.length());
		return destination.cast(paths.get(0).convert(o));
	}
	
	/**
	 * Returns the index of the given class in the map.
	 * If the class does not exist yet, an index is generated. 
	 * 
	 * @param clazz Class to get index of
	 * 
	 * @return Index of class
	 */
	protected int indexOf(Class<? extends T> clazz) {
		int index = order.indexOf(clazz);
		if(index >= 0)
			return index;
		order.add(clazz);
		if(map.length < order.size())
			resize();
		return order.size() - 1;
	}
	
	/** Expands the map to fit more classes in it */
	@SuppressWarnings("unchecked")
	protected void resize() {
		Path<T>[][] oldMap = map;
		map = new Path[oldMap.length + 5][oldMap.length + 5];
		
		for(int i = 0; i < oldMap.length; i++)
			for(int j = 0; j < oldMap.length; j++)
				map[i][j] = oldMap[i][j];
	}
	
	/**
	 * Adds a conversion to the map.
	 * 
	 * @param source      Conversion source
	 * @param destination Conversion destination
	 * @param converter   Filter to perform conversion from source type to destination type
	 */
	public void add(Class<? extends T> source, Class<? extends T> destination, Function<T, T> converter) {
		addPaths(source, destination, converter);
		
		// Create copy to prevent ConcurrentModificationException
		List<Class<? extends T>> possibleSubs = new ArrayList<>(order);
		
		for(Class<? extends T> sub : possibleSubs) {
			if(!source.isAssignableFrom(sub))
				continue;
			
			// sub extends source
			addPaths(sub, destination, converter);
			
			for(Class<?> inter : destination.getInterfaces()) {
				if(!classOfT.isAssignableFrom(inter))
					continue;
				Class<? extends T> sup = inter.asSubclass(classOfT);
				
				// destination implements sup
				addPaths(sub, sup, converter);
			}
			
			for(Class<?> cls = destination.getSuperclass(); cls != null; cls = cls.getSuperclass()) {
				if(!classOfT.isAssignableFrom(cls))
					break;
				Class<? extends T> sup = cls.asSubclass(classOfT);
				
				// destination extends sup
				addPaths(sub, sup, converter);
			}
		}
	}
	
	/**
	 * Adds a conversion to the map.<br>
	 * <i>Helper method for {@link #add(Class, Class, Function)}</i>
	 * 
	 * @param source      Conversion source
	 * @param destination Conversion destination
	 * @param converter   Filter to perform conversion from source type to destination type
	 */
	protected void addPaths(Class<? extends T> source, Class<? extends T> destination, Function<T, T> converter) {
		int convSrc = indexOf(source), convDst = indexOf(destination);
		
		// Source > New Converter > Destination
		map[convSrc][convDst] = new Path<T>(converter);
		
		// T > Old Converter > Source > New Converter > Destination
		for(int src = 0; src < order.size(); src++) {
			if(src == convDst || map[src][convSrc] == null)
				continue;
			
			Path<T> path = new Path<T>(map[src][convSrc], converter);
			if(map[src][convDst] == null || map[src][convDst].length() >= path.length()) {
				map[src][convDst] = path;
			}
		}
		
		// Source > New Converter > Destination > Old Converter > T
		for(int dst = 0; dst < order.size(); dst++) {
			if(dst == convSrc || map[convDst][dst] == null)
				continue;
			
			Path<T> path = new Path<T>(converter, map[convDst][dst]);
			if(map[convSrc][dst] == null || map[convSrc][dst].length() >= path.length()) {
				map[convSrc][dst] = path;
				
				// T > Old Converter > Source > New Converter > Destination > Old Converter > T
				for(int src = 0; src < order.size(); src++) {
					if(src == dst || map[src][convSrc] == null)
						continue;
					
					path = new Path<T>(map[src][convSrc], converter, map[convDst][dst]);
					if(map[src][dst] == null || map[src][dst].length() >= path.length()) {
						map[src][dst] = path;
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		String[][] sMap = new String[map.length][map.length];
		String[] classNames = new String[map.length];
		int width[] = new int[map.length];
		int classWidth = 0;
		
		// Collect class names
		for(int i = 0; i < order.size(); i++) {
			classNames[i] = order.get(i).getSimpleName();
			width[i] = classNames[i].length();
			classWidth = width[i] > classWidth ? width[i] : classWidth;
		}
		for(int i = order.size(); i < classNames.length; i++)
			classNames[i] = "";
		
		// Collect paths
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map.length; j++) {
				sMap[i][j] = i == j ? "--" : map[i][j] == null ? "<>" : map[i][j].toString();
				
				if(sMap[i][j].length() > width[j])
					width[j] = sMap[i][j].length();
			}
		}
		
		StringBuilder output = new StringBuilder();
		// Write first line (class names)
		output.append(String.format("%-" + classWidth + "s ", ""));
		for(int i = 0; i < classNames.length; i++) {
			output.append(' ');
			output.append(String.format("%-" + width[i] + "s", classNames[i]));
		}
		// Write other lines
		for(int i = 0; i < map.length; i++) {
			// Class name
			output.append(String.format("\n%-" + classWidth + "s [", classNames[i]));
			// Paths
			for(int j = 0; j < map[i].length; j++) {
				if(j > 0)
					output.append(';');
				output.append(String.format("%-" + width[j] + "s", sMap[i][j]));
			}
			output.append(']');
		}
		return output.toString();
	}
	
}
