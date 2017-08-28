package itb2.filter.property;

public class IntegerProperty extends FilterProperty {
	public int value;
	
	public IntegerProperty(String name, int value) {
		super(name);
		this.value = value;
	}

	@Override
	public Integer getValue() {
		return value;
	}

}
