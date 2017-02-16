package Project2.inference_method_4;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class FOLFCInference {
	
	private KnowledgeBase KB = null;		//tell
	public static AtomicSentence a = null;	//ask
	
	/*
	 * new_variables Map contains (String, String) pairs which contain the name of the new variables and their content.
	 * For example, in slides (slide 12 - inference example) the new variables and their content are:
	 * {x1/M1}
	 * {x2/Nono}
	 * {x3/M1}
	 * {x4/West}
	 * {y1/M1}
	 * {z1/Nono}  
	 */
	private Map<String, String> new_variables = new LinkedHashMap<String, String>(); 
	
	public static ArrayList<String> vars = new ArrayList<>(); 		//the variables of the Knowledge Base (for example: x, y, z)
	public static ArrayList<String> constants = new ArrayList<>();  //the constants of the Knowledge Base (for example: Nono, West, America, etc.)
	
	private ArrayList<Integer> counters = null; //counters for each variable (for example: x {1, 2, 3, 4}, y {1}, z {1})
	
	private ArrayList<Sentence> proofs = null;	//the proved sentences
	private Sentence toProve = null;			//the sentence that contains the ask atomic sentence
	
	/*
	 * Constructor
	 */
	public FOLFCInference(String tell, String ask)
	{
		KB = new KnowledgeBase(tell);
		a = new AtomicSentence(ask, false);
	}
	
	/*
	 * Start of the algorithm. 
	 * Prints the result of the inference algorithm.
	 * If true, return the proven sentence and the new variables created as the algorithm was executing.
	 */
	public void execute()
	{
		System.out.println("\n***Knowledge Base***");
		KB.printKB();
		System.out.println("\n***Atomic Sentence to prove***");
		a.print();
		System.out.println("\n\n");
		
		if(fol_fc_ask(KB, a))
			System.out.println("\n_______________________________________________________________________________\nResult: TRUE");
		else
			System.out.println("\n_______________________________________________________________________________\nResult: FALSE");
		
		toProve.print();
	    
		Iterator<Entry<String, String>> it = new_variables.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pair = (Map.Entry<String,String>)it.next();
	        System.out.println("{" + pair.getKey() + " = " + pair.getValue() + "}");
	    }
	}
	
	//First-Order Logic - Forward Chaining - for definite Horn Clauses
	public boolean fol_fc_ask(KnowledgeBase KB, AtomicSentence a)
	{
		toProve = KB.toProveSentence(a);			//sentence that contains the ask atomic sentence
		toProve.print();
		toProve.getVars();							//get the variables of the to-prove sentence, adds to the total vars of the KB 
		
		proofs = new ArrayList<>();					//initially, the facts of the KB 
		proofs.addAll(KB.getFacts());				//
		
		System.out.println("\n***Facts***");		//print Facts
		for(int i = 0; i < proofs.size(); i++)
		{
			proofs.get(i).getConstants();			//get the constants of the facts
			proofs.get(i).print();
		}
		ArrayList<Sentence> agenda = KB.getAgenda();//sentences to be proved
		System.out.println("\n***Agenda***");		//print Agenda
		for(int i = 0; i < agenda.size(); i++)
		{
			agenda.get(i).getVars();				//get the variables of the agenda sentence(s), adds to the total vars of the KB
			agenda.get(i).print();
		}
		
		initCounters();								//initialize the counters of the variables of the KB
				
		do
		{
			for(int i = 0; i < agenda.size(); i++)
			{
				new_vars(agenda.get(i));
				System.out.println("\n***Processing sentence in agenda...");
				agenda.get(i).print();
				
				ArrayList<AtomicSentence> atomics = agenda.get(i).getAtomic();
				int needed = atomics.size()-1; 	//how many atomic sentences needed to prove the specific sentence of agenda
				for(int j = 0; j < atomics.size() - 1; j++)
				{
					if(isProven(atomics.get(j)))
						needed--;
				}
				
				if(needed == 0) //the sentence is proven
				{
					proofs.add(new Sentence(atomics.get(atomics.size()-1).toString(), true)); //add to the proofs list
					agenda.remove(i);														  //remove from agenda
					i = 0;																	  //go to the top of agenda list again
				}else
					continue;																  //continue to check the next sentence
																							  //of the agenda
					
			}
		}while(!agenda.isEmpty()); //When the agenda is Empty, we can check if the ask can be proved.
		
	    new_vars(toProve);
	    proofs.add(new Sentence(toProve.getAtomic().get(toProve.getAtomic().size()-1).toString(), true));
	    
	    if(proofs.get(proofs.size()-1).comparesTo(new_variables, a)) //compare the proved atomic sentence to ask(user imput)
	    	return true;
	    else
	    	return false;
	}
	
	private void new_vars(Sentence s)
	{
		ArrayList<AtomicSentence> a = s.getAtomic();
		ArrayList<String> newp = null;					//new parameters of each atomic sentence
		ArrayList<String> oldp = new ArrayList<>();		//old parameters of each atomic sentence
		int index = -1;
		for(int i = 0; i < a.size(); i++)
		{
			ArrayList<Parameter> p = a.get(i).getParameters();
			oldp = findVars(p);								
			
			newp = new ArrayList<>();
			for(int k = 0; k < oldp.size(); k++)
			{
				index = getCIndex(oldp.get(k));
				newp.add(oldp.get(k) + counters.get(index));  //for example, oldp: x >> newp:x1
			}
			
			for(int j = 0; j < newp.size(); j++)
			{
				setValues(a.get(i), newp.get(j));
				if(!new_variables.containsKey(newp.get(j)))
					new_variables.put(newp.get(j), valueOfNewVar(s, newp.get(j))); //add a new variable, for example {x1, M1}
			}
		}
		//newp
		int val = counters.get(index).intValue();
		val++;
		counters.set(index, val);
	}
	
	/*
	 * 
	 */
	private String valueOfNewVar(Sentence s, String v)
	{
		ArrayList<AtomicSentence> a = s.getAtomic();
		for(int i = 0; i < proofs.size(); i++)
		{
			ArrayList<AtomicSentence> ap = proofs.get(i).getAtomic();
			for(int j = 0; j < a.size(); j++)
			{
				if(a.get(j).compareTo(ap.get(0)) && a.get(j).getParameters().size() == ap.get(0).getParameters().size())
				{//for each atomic sentence if the name is equal with a name of the proofs and their parameter lists have the same size
					ArrayList<Parameter> p = a.get(j).getParameters();
					for(int k = 0; k < p.size(); k++)
					{
						if(v.contains(p.get(k).getValue())) //if (x1 == x1)
						{ 
							if(new_variables.containsKey(ap.get(0).getParameters().get(k).getValue())) //if the value already exists 
								return new_variables.get(ap.get(0).getParameters().get(k).getValue()); //for example: M1, Nono
							else
								return ap.get(0).getParameters().get(k).getValue();					  //return the value of the matching proof in proofs list
						}
						else
						{
							continue;
						}
					}//end of for(k)
				}
			}//end of for(j)
		}//end of for(i)
		return null; //if no match, return null
	}
	
	/*
	 * Checks if there is any variable in the parameters list
	 * Returns the list of the (distinct) variables found
	 */
	private ArrayList<String> findVars(ArrayList<Parameter> p)
	{
		ArrayList<String> s = new ArrayList<String>();
		for(int i = 0; i < p.size(); i++)
		{
			if((!s.contains(p.get(i).getName())) && vars.contains(p.get(i).getName()))
				s.add(p.get(i).getName());
		}
		return s;
	}
	
	/*
	 * Initialize the counters of each variable in vars list (defined at the top of the FOLFCInference class)
	 * Usually, the initial state will be something like this:
	 * vars   counters
	 * ----   --------
	 *  x		1
	 *  y		1
	 *  z		1
	 */
	public void initCounters()
	{
		counters = new ArrayList<Integer>();
		for(int i = 0; i < vars.size(); i++)
			counters.add(1);
	}
	
	/*
	 * the index of the counter for a given variable
	 * vars and counters lists have the same number of lines
	 * vars   counters
	 * ----   --------
	 *  x		1 <--CIndex(=0) -- if (v.name == 'x')
	 *  y		1 <--CIndex(=1) -- if (v.name == 'y')
	 *  z		1 <--CIndex(=2) -- if (v.name == 'z')
	 */
	public int getCIndex(String v)
	{
		int index = -1;
		for(int i = 0; i < vars.size(); i++)
		{
			if(vars.get(i).equals(v))
				index = i;
		}
		return index;
	}
	
	/*
	 * For each Parameter, we have:
	 * name (for example: x or y or ...)
	 * value (for example: x1 or x2 or ...)
	 * Each time new_vars function is executed the value of each parameter(variable) changes
	 */
	private void setValues(AtomicSentence a, String val)
	{
		ArrayList<Parameter> p = a.getParameters();
		for(int i = 0; i < p.size(); i++)
		{
			if(vars.contains(p.get(i).getName()) && val.contains(p.get(i).getName()))
				p.get(i).setValue(val);
		}
	}
	
	/*
	 * Checks if the given atomic Sentence is proven in proofs list
	 * Checks for:
	 * name
	 * value of parameters
	 * to determine if it is proven or not
	 */
	private boolean isProven(AtomicSentence a)
	{
		boolean prooved = false;
		for(int i = 0; i < proofs.size(); i++)
		{
			if(proofs.get(i).getAtomic().get(0).getName().equals(a.getName()))
			{ //name
				prooved = true;
				ArrayList<Parameter> p = proofs.get(i).getAtomic().get(0).getParameters();
				for(int j = 0; j < p.size(); j++)
				{
					if(new_variables.containsValue(p.get(j).getName()))
					{//checks if the parameter has a value in new_variables map and compares with this value
						if(p.get(j).getName().equals(new_variables.get(a.getParameters().get(j).getValue())))
							prooved = true;
						else
							prooved = false;
					}
					else
					{
						if(p.get(j).getName().equals(a.getParameters().get(j).getValue()))
							prooved = true;
						else
							prooved = false;
					}
				}
			}
			else
			{
				prooved = false;
			}
			if(prooved)
				return prooved;
		}
		return prooved;
	}
	
	public static void main(String[] args) throws IOException {
		String ask = "", tell;
		String KBpath = "/home/tom/workspace/AI/src/Project2/inference_method_4/FOLKBtest1.txt";
		Parser parser = new Parser();
		tell = parser.parse(KBpath);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("First-order Logic Inferences (Forward Chaining) for Horn Clauses");
        System.out.print("Enter the atomic sentence you want to prove: ");
        try {
			ask = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
        br.close();
        FOLFCInference f = new FOLFCInference(tell, ask);	
        f.execute();
	}

}