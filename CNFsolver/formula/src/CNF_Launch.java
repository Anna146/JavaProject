


import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.runtime.ANTLRInputStream;

import org.antlr.v4.runtime.CommonTokenStream;

import org.antlr.v4.runtime.tree.ParseTree;

import org.antlr.v4.gui.TreeViewer;


public class CNF_Launch {
	public static void main(String[] args) throws Exception {
		
		file_handling file_io = new file_handling();
		ANTLRInputStream stream = new ANTLRInputStream(file_io.read());
        FormulaLexer lexer  = new FormulaLexer(stream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        FormulaParser parser = new FormulaParser(tokenStream);
        
        ParseTree tree = parser.formula();
        
        //show AST in console
        //System.out.println(tree.toStringTree(parser));

        //show AST in GUI
        JFrame frame = new JFrame("Antlr AST");
        JPanel panel = new JPanel();
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                parser.getRuleNames()),tree);
        viewr.setScale(1.0);//scale a little
        panel.add(viewr);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setVisible(true);
        
        if(parser.getNumberOfSyntaxErrors() != 0)
        {
        	System.out.println("Sytax Error, Cannot Proceed");
        	return;
        }
        else
        {
        	Tree_Visitor tree_visitor = new Tree_Visitor();
        	Formula final_formula = tree_visitor.visit(tree);
        	final_formula = final_formula.optimize(final_formula);
        	file_io.write_cnf_human_readable(final_formula.clause_vector);
        	System.out.println("I am done with CNF Writing");
        	Vector<String> final_result = new Vector<String>();
        	file_io.delete_files();
        	int index = 0;
        	if(final_formula.clause_vector.size() == 1 &&(final_formula.clause_vector.get(0).equals("True")
        			||final_formula.clause_vector.get(0).equals("False")))
        	{
        		if(final_formula.clause_vector.get(0).equals("True"))
        		{
        			System.out.println("This is a tautology");
        			Vector<String> tauto_models = new Vector<String>();
        			tauto_models = generate_models(tree_visitor.tokens_hash);
        			Vector<String> v = new Vector<String>();
        			v.add("sat");
        			tauto_models = combine_vectors(v,tauto_models);
        			file_io.write_answer(tauto_models);
        			//somewhere write this tauto models
        		}
        		else
        		{
        			System.out.println("This is unsatisfiable");
        			Vector<String> unsat = new Vector<String>();
        			unsat.add("unsat");
        			file_io.write_answer(unsat);
        		}
        	}
        	else
        	{
        		//calling z3
        		Vector<String> answer_to_write = new Vector<String>();
        		Vector<String> extra_token_models = new Vector<String>();
        		Hashtable<String,Integer> hash_token_diff = new Hashtable<String,Integer>();
        		Vector<String> input_file_array = new Vector<String>();
        		do
        		{
        		
        			if(index == 0)
        			{
        				input_file_array = file_io.genInput(final_formula.clause_vector);
        			}
        				
        			else
        			{
        				input_file_array = file_io.new_input(input_file_array,index);
        			}
        			file_io.write_z3_input_file(input_file_array,index);
        			final_result = file_io.call_z3_solver(index);
        			if(final_result.get(0).equals("sat") && tree_visitor.tokens_hash.isEmpty() == false) //file_io.z3_map.isEmpty() == false means that the formula is unsat
        			{
        				hash_token_diff = calculate_diff(tree_visitor.tokens_hash,file_io.z3_map);
        			}
        			if(hash_token_diff.size() > 0 && final_result.get(0).equals("sat"))
        			{
        				extra_token_models = generate_models(hash_token_diff);
        				answer_to_write = combine_vectors(final_result,extra_token_models);
        			}
        			else
        			{
        				answer_to_write = final_result;
        			}
        			file_io.write_answer(answer_to_write);
        			++index;
        			
        		}while(final_result.get(0).equals("sat"));
        		
        	}
        }
    }
	
	public static Vector<String> generate_models(Hashtable<String,Integer> tokens_hash)
	{
		Vector<String> token_vector = new Vector<String>();
		if(tokens_hash.isEmpty())
		{
			System.out.println("You haven't entered any term");
		}
		for(String key : tokens_hash.keySet())
		{
			token_vector.add(key);
		}
		Vector<String> answer_vector = new Vector<String>();
		Vector<String> final_vector = new Vector<String>();
		recur(token_vector,-1, "",answer_vector,true,final_vector);
		return final_vector;
	}
	
	public static void recur(Vector<String> token_vector,int length, String Value,Vector<String> answer_vector,boolean print, Vector<String> final_vector)
	{
		if(length >= token_vector.size())
		{
			if(print)
			{
				for(int i=0;i<answer_vector.size();++i)
				{
					final_vector.add(token_vector.get(i) + " = " + answer_vector.get(i));
				}
				final_vector.add("\n");
			}
			return;
		}
		if(!Value.equals(""))
		{
			if(answer_vector.size()>length)
			{
				answer_vector.set(length, Value);
			}
			else
			{
				answer_vector.add(Value);
			}
		}
		recur(token_vector,length+1,"False",answer_vector,true,final_vector);
		recur(token_vector,length+1,"True",answer_vector,false,final_vector);
	}
	
	public static  Hashtable<String,Integer> calculate_diff(Hashtable<String,Integer> tokens_hash,Map<String,Integer> clause_map)
	{
		Hashtable<String,Integer> answer = new Hashtable<String,Integer>();
		for(String s : tokens_hash.keySet())
		{
			if(clause_map.containsKey(s) == false)
			{
				answer.put(s, 1);
			}
		}
		return answer;
	}
	
	public static Vector<String> combine_vectors(Vector<String> v1, Vector<String> v2)
	{
		//v1 contains the solved output from the z3 and v2 contains the remaining tokens, v2 has a new line for each set of values
		if(v2.size() == 0)
		{
			return v1;
		}
		else
		{
			Vector<String> final_vector = new Vector<String>();
			final_vector.addAll(v1);
			for(int i=0;i<v2.size();++i)
			{
				if(v2.get(i).contains("\n"))
				{
					final_vector.add("\n");
					if(i!=(v2.size()-1))
					{
						final_vector.addAll(v1);
					}
				}
				else
				{
					final_vector.add(v2.get(i));
				}
			}
			return final_vector;
			
		}
	}
	
}
