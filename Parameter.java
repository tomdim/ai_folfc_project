package Project2.inference_method_4;

public class Parameter {
	private String name;
	private String value = null;
	
	public Parameter(String name, String initialValue)
	{
		this.name = name;
		this.value = initialValue;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void print()
	{
		System.out.print(value);
	}
}
