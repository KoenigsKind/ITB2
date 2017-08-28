package itb2.filter.property;

public abstract class FilterProperty {
	private static int nextIndex = 0;
	public final int index;
	public final String name;
	
	public FilterProperty(String name) {
		this.index = nextIndex++;
		this.name = name;
	}
	
	public abstract Object getValue();
}
