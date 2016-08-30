import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

public class Formula {
	public String type;
	public Vector <String> clause_vector;
	
	
	public Formula(){
		clause_vector = new Vector <String>();
		type = new String();
        }
        
    public Formula (String type, Vector <String> clauses) {
    	this.clause_vector = new Vector<String>(clauses);
    	this.type = new String(type);
	}
    
    public Formula (Formula f)
    {
    	this.clause_vector = new Vector<String>(f.clause_vector);
    	this.type = new String(f.type);
    }
    
    public boolean equals(Formula f)
    {
    	if(this.type != f.type)
    	{
    		return false;
    	}
    	if(f.clause_vector.size()!=this.clause_vector.size())
    	{
    		return false;
    	}
    	for(int i=0;i<f.clause_vector.size();++i)
    	{
    		String s = f.clause_vector.get(i);
    		String s1 = this.clause_vector.get(i);
    		if(!s.equals(s1))
    		{
    			return false;
    		}
    	}
    	return true;
    }
    
    public boolean compare_vector(Vector<String> vec1, Vector<String> vec2)
    {
    	if(vec1.size()!=vec2.size())
    	{
    		return false;
    	}
    	for(int i=0;i<vec1.size();++i)
    	{
    		String s = vec1.get(i);
    		String s1 = vec2.get(i);
    		if(s.length()!=s1.length())
    		{
    			return false;
    		}
    		String[] temp = s.split("\\$");
    		String[] temp1 = s1.split("\\$");
    		if(temp.length!=temp1.length)
    		{
    			return false;
    		}
    		Hashtable <String,Integer> cl_hash = new Hashtable <String,Integer>();
    		for(int j=0;j<temp.length;++j)
    		{
    			if(cl_hash.containsKey(temp[j]))
    			{
    				int value = cl_hash.get(temp[j]);
    				cl_hash.replace(temp[j], value+1);
    			}
    			else
    			{
    				cl_hash.put(temp[j], 1);
    			}
    			
    			if(cl_hash.containsKey(temp1[j]))
    			{
    				int value = cl_hash.get(temp1[j]);
    				cl_hash.replace(temp1[j], value-1);
    			}
    			else
    			{
    				cl_hash.put(temp1[j], -1);
    			}	
    		}
    		for(String key:cl_hash.keySet())
    		{
    			if(cl_hash.get(key)!=0)
    			{
    				return false;
    			}
    		}
    	}
    	return true;
    }
    public Formula do_disjunction(Formula f1, Formula f2)
    {
    	if((f1.type.equals(f2.type)))
    	{
    		if(f1.clause_vector.size() >= f2.clause_vector.size())
    		{
    			Formula f = new Formula("expression",disj_clause(f1,f2));
    			return f;
    		}
    		else
    		{
    			Formula f = new Formula("expression",disj_clause(f2,f1));
    			return f;
    		}
    	}
    	else
    	{
    		Formula f = new Formula();
    		f.type = "expression";
    		f.clause_vector.add("False");
    		return f;
    	}
    }
    
    public Vector<String> disj_clause(Formula f1, Formula f2)
    {
    	for(int i=0;i<f1.clause_vector.size();++i)
    	{
    		for(int j=0;j<f2.clause_vector.size();++j)
    		{
    			String s = f1.clause_vector.get(i) + "$" + f2.clause_vector.get(j);
    			f1.clause_vector.setElementAt(s, i);
    		}
    	}
    	return f1.clause_vector;
    }
    
    public Formula do_conjunction(Formula f1, Formula f2)
    {
    	if((f1.type.equals(f2.type)))
    	{
    		if(f1.clause_vector.size() < f2.clause_vector.size())
    		{
    			Formula f = new Formula("expression",conj_clause(f1,f2));
    			return f;
    		}
    		else
    		{
    			Formula f = new Formula("expression",conj_clause(f2,f1));
    			return f;
    		}
    	}
    	else
    	{
    		Formula f = new Formula();
    		f.type = "expression";
    		f.clause_vector.add("False");
    		return f;
    	}
    }
    
    public Vector<String> conj_clause(Formula f1, Formula f2)
    {
    	f2.clause_vector.addAll(f1.clause_vector);
    	return f2.clause_vector;
    }
    
    public Formula do_implication(Formula f1, Formula f2)
    {
    	f1 = f1.do_negation(f1);
		return do_disjunction(f1, f2);
    }
    public Formula do_negation(Formula f1)
    {
    	if(f1.type.equals("expression"))
    	{
    	if(f1.clause_vector.size() == 1 && (f1.clause_vector.get(0).length() == 1 || f1.clause_vector.get(0).length() == 2))// && matrix.get(0).length == 1)
    	{
    		return apply_negation(f1);
    	}
    	else
    	{
    		int clv_size = f1.clause_vector.size();
    		Vector<String[]> matrix = new Vector<String[]>(clv_size);
    		for(int i=0;i<clv_size;++i)
    		{
    			matrix.add(f1.clause_vector.get(i).split("\\$"));
    		}
    		Vector<String> temp_vector1 = new Vector<String>();
    		Collections.addAll(temp_vector1, matrix.get(0));
    		Formula temp_form1 = new Formula("expression",temp_vector1);
    		for(int i=1;i<clv_size;++i)
    		{
    			Vector<String> temp_vector2 = new Vector<String>();
    			Collections.addAll(temp_vector2, matrix.get(i));
    			Formula temp_form2 = new Formula("expression",temp_vector2);
    			temp_form1 = do_disjunction(temp_form1,temp_form2);
    		}
    		temp_form1 = apply_negation(temp_form1);
    		return temp_form1;
    	}
    	}
    	else
    	{
    		Formula f = new Formula();
    		f.type = "expression";
    		f.clause_vector.add("False");
    		return f;
    	}
    	
    }
    public Formula apply_negation(Formula f1)
    {
    	if(f1.type.equals("expression"))
    	{
    	if(f1.clause_vector.size() == 1 && (f1.clause_vector.get(0).length() == 1 || f1.clause_vector.get(0).length() == 2 ||f1.clause_vector.get(0).equals("True") || f1.clause_vector.get(0).equals("False")))
    	{
    		String s = new String(f1.clause_vector.get(0));
    		if(s.equals("True")||s.equals("False"))
    		{
    			if(s.equals("True"))
    			{
    				s="False";
    			}
    			else
    			{
    				s="True";
    			}
    		}
    		else if(s.startsWith("~"))
    		{
    			s=s.substring(1);
    		}
    		else
    		{
    			s="~" + s;
    		}
    		
    		f1.clause_vector.setElementAt(s, 0);
    		
    		return f1;
    	}
    	else
    	{
    		int clv_size = f1.clause_vector.size();
    		Vector<String[]> matrix = new Vector<String[]>(clv_size);
    		for(int i=0;i<clv_size;++i)
    		{
    			matrix.add(f1.clause_vector.get(i).split("\\$"));
    		}
    		for(int i=0;i<clv_size;++i)
    		{
    			int clause_length = matrix.get(i).length;
    			for(int j=0;j<clause_length;++j)
    			{
    				String s = (matrix.get(i))[j];
    				if(s.startsWith("~"))
    	    		{
    	    			s=s.substring(1);
    	    		}
    	    		else
    	    		{
    	    			s="~" + s;
    	    		}
    				(matrix.get(i))[j] = s;
    			}
    		}
    		
    		Vector <String> str_vec = new Vector <String>();
    		for(int i=0;i<clv_size;++i)
    		{
    			int clause_length = matrix.get(i).length;
    			String s = matrix.get(i)[0];
    			for(int j=1;j<clause_length;++j)
    			{
    				s+= "$" + matrix.get(i)[j];
    			}
    			str_vec.addElement(s);
    		}
    		Formula f = new Formula("expression",str_vec);
    		return f;
    	}
    	}
    	else
    	{
    		Formula f = new Formula();
    		f.type = "expression";
    		f.clause_vector.add("False");
    		return f;
    	}
    }
    
    public Formula optimize(Formula f)
    {
    	//Formula f1 = new Formula(f);
    	//Vector<String> prev_clause_vec = new Vector<String>(f.clause_vector);
    	
    		//System.out.println("hello");
    		//prev_clause_vec.clear();
    		//prev_clause_vec.addAll(f.clause_vector);
    	Vector<String> prev_vec = new Vector<String>();
    	while(!compare_vector(prev_vec,f.clause_vector))
    	{
    		prev_vec.clear();
    		prev_vec.addAll(f.clause_vector);
    		f = clean_clauses(f);
    		f = superset_removal(f);
    		f.clause_vector = unit_propagation(f.clause_vector);
    	}
    	return f;
    	
    }
    //This function removes true clauses, removes false and minimizes the clauses if there is dublication
    public Formula clean_clauses(Formula f1)
    {
    	//Checks whether this formula is a literal or not
    	if(f1.clause_vector.size() == 1 && (f1.clause_vector.get(0).length() == 1 || f1.clause_vector.get(0).length() == 2))
    	{
    		return f1;
    	}
    	//if not then we proceed
    	else
    	{
    		int clv_size = f1.clause_vector.size();
    		Vector<String[]> matrix = new Vector<String[]>();
    		for(int i=0;i<clv_size;++i)
    		{
    			//We continue if we encounter a true value and we do not split the clauses 
    			if(f1.clause_vector.get(i).contains("True") || f1.clause_vector.get(i).contains("~False"))
    			{
    				continue;
    			}
    			matrix.add((f1.clause_vector.get(i)).split("\\$"));
    		}
    		if(matrix.size() == 0)
    		{
    			Formula f = new Formula();
				f.clause_vector.add("True");
				f.type = "expression";
				return f;
    		}
    		Vector<String> str_vec = new Vector<String>();
    		for(int i=0;i<matrix.size();++i)
    		{
    			int clause_length = (matrix.get(i)).length;
    			Hashtable<String,Integer> clause_hash = new Hashtable<String,Integer>();
    			boolean has_put = false;
    			for(int j=0;j<clause_length;++j)
    			{
    				String s = (matrix.get(i))[j];
    				//while splitting we check if it contains the False clause, if yes then we skip this
    				if(s.equals("False") || s.equals("~True"))
    				{
    					continue;
    				}
    				if(clause_hash.containsKey(s) == false)
    				{
    					has_put = true;
    					clause_hash.put(s,1);
    				}
    			}
    			if(has_put == false)
    			{
    				Formula f = new Formula();
    				f.clause_vector.add("False");
    				f.type = "expression";
    				return f;
    			}
    			boolean turn_first = true;
    			String str = "";
    			for (String key : clause_hash.keySet())
    			{
    				if(turn_first)
    				{
    					str+=key;
    					turn_first = false;
    				}
    				else
    				{
    					str+= "$" + key;
    				}
    			}
    			str_vec.add(str);
    		}
    		Formula f = new Formula("expression",str_vec);
    		return f;
    	}
    }
    
    public Formula superset_removal(Formula f)
	{
		int vector_size = f.clause_vector.size();
		Vector<Integer> indices = new Vector<Integer>();
		
		//makes note of which clauses we have to remove and stores it in the indices vector. Now after
		//iteration we drop those clauses
		for(int i=0;i<vector_size-1;++i)
		{
			if(indices.contains(i))
			{
				continue;
			}
			String[] temp_str = f.clause_vector.get(i).split("\\$");
			for(int j=i+1;j<vector_size;++j)
			{
				if(indices.contains(j))
				{
					continue;
				}
				Hashtable<String,Integer> clause_hash = new Hashtable<String,Integer>();
				for(String s: temp_str)
				{
					if(clause_hash.containsKey(s) == false)
					{
						clause_hash.put(s,1);
					}
					else
					{
						int value = (int)clause_hash.get(s);
						clause_hash.replace(s, value+1);
						//clause_hash.put(s,value+1);
					}
				}
				String[] temp_str1 = f.clause_vector.get(j).split("\\$");
				for(String s: temp_str1)
				{
					if(clause_hash.containsKey(s) == false)
					{
						clause_hash.put(s,-1);
					}
					else
					{
						int value = (int)clause_hash.get(s);
						clause_hash.replace(s, value-1);
						//clause_hash.put(s,value+1);
					}
				}
				boolean[] val_arr = new boolean[2];
				for (String key : clause_hash.keySet())
				{
					int value = (int)clause_hash.get(key);
					if(value == -1)
					{
						val_arr[0] = true;
					}
					else if(value == 1)
					{
						val_arr[1] = true;
					}
				}
				if(val_arr[0] == false && val_arr[1] == false)
				{
					indices.add(j);
				}
				else if(val_arr[0] == false && val_arr[1] == true)
				{
					indices.add(i);
				}
				else if(val_arr[0] == true && val_arr[1] == false)
				{
					indices.add(j);
				}
			}
		}
		Vector<String> temp = new Vector<String>();
		for(int i=0;i<f.clause_vector.size();++i)
		{
			if(indices.contains(i))
			{
				continue;
			}
			else 
			{
				temp.add(f.clause_vector.get(i));
			}
		}
		Formula f1 = new Formula(f.type,temp);
		return f1;
	}
    
    public static Vector<String[]> prop(Vector<String[]> clauses, String var) {
		Vector<String[]> res = new Vector<String[]>();
		for (int i=0; i<clauses.size(); ++i) {
			int add = 1;
			String[] curr = clauses.get(i);
			//String[] newCurr;
			ArrayList<String> newcl = new ArrayList<String>();
			for (int j=0; j<curr.length; ++j) {
				String curvar = curr[j];
				if (var.equals(curvar)) 
					add = 0;
				if (!(var.equals("~" + curvar) || curvar.equals("~" + var))) {
					newcl.add(curvar);
				}
			}
			if (newcl.size() == 0) {
				String[] fl = {"False"};
				res.add(fl);
				return res;
				}
			if (add == 1) 
				res.add(newcl.toArray(new String[newcl.size()]));
			
		}
		return res;
	}
	
	public static Vector<String> unit_propagation(Vector<String> clauses) {
		Vector<String> res = new Vector<String>();
		int no_unary = 0;
		Vector<String[]> splited = new Vector<String[]>();
		Vector<String[]> units = new Vector<String[]>();
		for (int i=0; i<clauses.size(); ++i) {
			String curr = clauses.get(i).replace("~True", "False").replace("~False", "True");
			if (curr.equals("False")) {
				res.add("False");
				return res;
			}
			if (!(curr.contains("True"))) {
				String[] rarr = curr.replace("False", "").split("\\$");
				splited.add(rarr);
			}
		}
		while (no_unary == 0) {
			no_unary = 1;
			for (int i=0; i<splited.size(); ++i) {
				String[] curr = splited.get(i);
				if (curr.length == 0) {
					res.add("False");
					return res;
				}
				if (curr.length == 1) {
					int pos = splited.size();
					units.add(curr);
					splited = prop(splited, curr[0]);
					if (splited.size() > 0 && splited.get(splited.size()-1)[0].equals("False")) {
						res.add("False");
						return res;
					}
					i -= pos - splited.size();
					no_unary = 0;
				}
			}
		}
		splited.addAll(units);
		for (int i=0; i<splited.size(); ++i) {
			res.add(String.join("$", splited.get(i)));
		}
		if (res.size() == 0)
			res.add("True");
		return res;
	}
    /*public Formula unit_prop(Formula f)
    {
    	Hashtable<String,Integer> indices_hash = new Hashtable<String,Integer>();
    	Vector<String> indices = new Vector<String>();
    	boolean first_time = true;
    	boolean vector_add = false;
    	while(first_time || vector_add)
    	{
    		vector_add = false;
    		first_time = false;
    		for(int i=0;i<f.clause_vector.size();++i)
    		{
    			String curr = f.clause_vector.get(i).replace("~True", "False").replace("~False", "True");
    			if (curr.equals("False"))
    			{
    				Formula f1 = new Formula();
    				f1.clause_vector.add("False");
    				f1.type = "expression";
    				return f1;
    			}
    			if(curr.equals("True"))
    			{
    				f.clause_vector.remove(i);
    				--i;
    			}
    			String[] curr_array = curr.split("\\$");
    			if(curr_array.length == 1)
    			{
    				if(indices_hash)
    				indices.add(curr_array[0]);
    				vector_add = true;
    			}
    		}
    		for(int i=0;i<)
    	}
    }*/
}