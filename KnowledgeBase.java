package Project2.inference_method_4;

import java.util.*;

public class KnowledgeBase {

	private ArrayList<Sentence> sentences = null;
	private ArrayList<Sentence> agenda = null;
	private ArrayList<Sentence> facts = null;
	
	public KnowledgeBase(String tell)
	{
		sentences = new ArrayList<Sentence>();
		agenda = new ArrayList<Sentence>();
		facts = new ArrayList<Sentence>();
		analyzeTell(tell);
	}
	
	private void analyzeTell(String tell)
	{
		String[] splitted = tell.split(";");
    	for (int i = 0; i < splitted.length; i++)
    	{
    		if (!splitted[i].contains("=>"))
    		{
    			Sentence s = new Sentence(splitted[i], true);
    			sentences.add(s);
    			facts.add(s);
    		}
    		else
    		{
    			Sentence s = new Sentence(splitted[i], false);	// add sentences
    			sentences.add(s);
    			agenda.add(s);									// add facts to be processed
    		}
    	}
	}
	
	public Sentence toProveSentence(AtomicSentence ask)
	{
		Sentence toProve = null;
		for(int i = 0; i < sentences.size(); i++)
		{
			ArrayList<AtomicSentence> atomic = sentences.get(i).getAtomic();
			for(int j = 0; j < atomic.size(); j++)
			{
				if(atomic.get(j).getName().equals(ask.getName()))
				{
					toProve = sentences.get(i);
					agenda.remove(sentences.get(i));
				}
			}
		}
		
		if(toProve == null)
		{
			System.err.println("Not valid KB or/and ask: " + ask + " does not exist in KB!");
			System.exit(-1);
		}
		
		return toProve;
	}
	
	public void printKB()
	{
		for(int i = 0; i < sentences.size(); i++)
		{
			sentences.get(i).print();
			System.out.println("");
		}
	}

	public ArrayList<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(ArrayList<Sentence> sentences) {
		this.sentences = sentences;
	}

	public ArrayList<Sentence> getAgenda() {
		return agenda;
	}

	public void setAgenda(ArrayList<Sentence> agenda) {
		this.agenda = agenda;
	}

	public ArrayList<Sentence> getFacts() {
		return facts;
	}

	public void setFacts(ArrayList<Sentence> facts) {
		this.facts = facts;
	}
	
}
