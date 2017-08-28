package itb2.filter.property;

public class StringProperty extends FilterProperty {
	public String value;
	
	public StringProperty(String name, String value) {
		super(name);
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

}
