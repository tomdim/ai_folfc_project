package Project2.inference_method_4;

import java.util.ArrayList;
import java.util.Map;

public class Sentence{
	private boolean fact;
	private int premiseCount;
	private ArrayList<AtomicSentence> atomic = null;
	
	public Sentence(String sentence, boolean fact)
	{
		this.fact = fact;
		this.atomic = new ArrayList<AtomicSentence>();
		analyzeSentence(sentence);
	}
	
	private void analyzeSentence(String s)
	{
		int start, end;
		String[] premises;
		if(fact)
		{
			atomic.add(new AtomicSentence(s, fact));
		}
		else
		{
			String[] splitted = s.split("=>");
			if(splitted[0].startsWith("("))
			{
				start = splitted[0].indexOf("(");
				end = splitted[0].lastIndexOf(")");
				premises = splitted[0].substring(start+1, end-1).split("∧");
			}
			else 
			{
				premises = splitted[0].split("∧");
			}
			setPremiseCount(premises.length);
			for(int i = 0; i < premises.length; i++)
				atomic.add(new AtomicSentence(premises[i].replaceAll("\\s+",""), fact));
			
			atomic.add(new AtomicSentence(splitted[1].replaceAll("\\s+",""), fact));
		}
	}
	
	public void getConstants() //finds the constants of each atomic sentence of the sentence
	{
		ArrayList<AtomicSentence> a = this.getAtomic();
		
		for(int i = 0; i < a.size(); i++)
		{
			ArrayList<Parameter> p = a.get(i).getParameters();
			for(int j = 0; j < p.size(); j++)
			{
				if(!FOLFCInference.constants.contains(p.get(j).getName()) && !FOLFCInference.vars.contains(p.get(j).getName()))
					FOLFCInference.constants.add(p.get(j).getName());
			}
		}
	}
	
	public void getVars() //finds the variables of each atomic sentence of the sentence
	{
		ArrayList<AtomicSentence> a = this.getAtomic();
		
		for(int i = 0; i < a.size(); i++)
		{
			ArrayList<Parameter> p = a.get(i).getParameters();
			for(int j = 0; j < p.size(); j++)
			{
				if(!FOLFCInference.a.getParameters().contains(p.get(j).getName()))
				{
					if(!FOLFCInference.vars.contains(p.get(j).getName()))
				
					{
						if(!FOLFCInference.constants.isEmpty())
						{
							if(!FOLFCInference.constants.contains(p.get(j).getName()))
								FOLFCInference.vars.add(p.get(j).getName());
						}
						else
						{
							FOLFCInference.vars.add(p.get(j).getName());
						}
							
					}
				}
			}
		}
	}

	public ArrayList<AtomicSentence> getAtomic() {
		return atomic;
	}

	public void setAtomic(ArrayList<AtomicSentence> atomic) {
		this.atomic = atomic;
	}

	public boolean isFact() {
		return fact;
	}

	public void setFact(boolean fact) {
		this.fact = fact;
	}

	public int getPremiseCount() {
		return premiseCount;
	}

	public void setPremiseCount(int premiseCount) {
		this.premiseCount = premiseCount;
	}
	
	public void print()
	{
		if(!fact)
			System.out.print("(");
		for(int i = 0; i < atomic.size()-1; i++)
		{
			atomic.get(i).print();
			if(i < atomic.size()-2)
				System.out.print(" ∧ ");
		}
		
		if(!fact)
			System.out.print(") => ");
		atomic.get(atomic.size()-1).print();
		System.out.print("\n");
	}
	
	public boolean comparesTo(Map<String, String> nv, AtomicSentence ask)
	{
		AtomicSentence proven = this.getAtomic().get(0);
		if(proven.getName().equals(ask.getName()))
		{
			for(int i = 0; i < proven.getParameters().size(); i++)
			{
				if(nv.get(proven.getParameters().get(i).getValue()).equals(ask.getParameters().get(i).getValue()) || nv.get(nv.get(proven.getParameters().get(i).getValue())).equals(ask.getParameters().get(i).getValue()))
					return true;
				else
					return false;
			}
		}
		return true;
		
	}
	
}
