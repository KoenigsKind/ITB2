package itb2.filter.property;

public class BooleanProperty extends FilterProperty {
	public boolean value;
	
	public BooleanProperty(String name, boolean value) {
		super(name);
		this.value = value;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

}
