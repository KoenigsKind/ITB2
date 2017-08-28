package itb2.filter.property;

public class DoubleProperty extends FilterProperty {
	public double value;
	
	public DoubleProperty(String name, double value) {
		super(name);
		this.value = value;
	}
	
	@Override
	public Double getValue() {
		return value;
	}
	
}
