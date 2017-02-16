package Project2.inference_method_4;

import java.util.ArrayList;

public class AtomicSentence {
	private String name = null;
	private boolean fact = false;
	private int premises = 0;
	private ArrayList<Parameter> parameters = null;
	
	public AtomicSentence(String atomic, boolean fact)
	{
		this.fact = fact;
		this.parameters = new ArrayList<Parameter>();
		analyzeAtomic(atomic);
	}
	
	private void analyzeAtomic(String s)
	{
		String params = null;
		int pos = -1;
		pos = s.indexOf('(');
		if(pos == -1)
			System.err.println("Not valid syntax for sentence: " + s);
		
		//System.out.println(s);
		this.name = s.substring(0, pos).replaceAll("\\s+","");
		//System.out.println(name);
		params = s.substring(pos+1, s.length()-1).replaceAll("\\s+","");
		//System.out.println(params);
		
		if(params.contains(","))
			analyzeParams(params);
		else
			parameters.add(new Parameter(params, params));
	}
	
	private void analyzeParams(String p)
	{
		String[] params = p.split(",");
		for(int i = 0; i < params.length; i++)
			parameters.add(new Parameter(params[i], params[i]));
	}

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<Parameter> parameters) {
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFact() {
		return fact;
	}

	public void setFact(boolean fact) {
		this.fact = fact;
	}

	public int getPremises() {
		return premises;
	}

	public void setPremises(int premises) {
		this.premises = premises;
	}
	
	public void print()
	{
		System.out.print(name + "(");
		//System.out.print(name + ": ");
		for(int i = 0; i < parameters.size(); i++)
		{
			parameters.get(i).print();
			if(i < parameters.size()-1)
			{
				System.out.print(", ");
			}
		}
		System.out.print(")");
	}
	
	public boolean compareTo(AtomicSentence a)
	{
		if(this.getName().equals(a.getName()))
			return true;
		else
			return false;
	}
	
	public String toString()
	{
		String out = this.name + "(";
		for(int i = 0; i < this.getParameters().size(); i++)
		{
			out += this.getParameters().get(i).getValue();
			if(i < this.getParameters().size()-1)
			{
				out += ", ";
			}
		}
		out += ")";
		//System.out.println(out);
		return out;
	}
}
