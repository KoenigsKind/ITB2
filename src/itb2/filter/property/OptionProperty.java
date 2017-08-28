package itb2.filter.property;

public class OptionProperty extends FilterProperty {
	public Object value;
	public Object[] options;
	
	public OptionProperty(String name, Object value, Object[] options) {
		super(name);
		this.value = value;
		this.options = options;
	}

	@Override
	public Object getValue() {
		return value;
	}

}
