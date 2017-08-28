package itb2.filter.property;

public class RangeProperty extends FilterProperty {
	public int value, min, step, max;
	
	public RangeProperty(String name, int value, int min, int step, int max) {
		super(name);
		this.value = value;
		this.min = min;
		this.step = step;
		this.max = max;
	}

	@Override
	public Integer getValue() {
		return value;
	}

}
