package optim;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class optimizer {
	
	public static Vector<String[]> prop(Vector<String[]> clauses, String var) {
		Vector<String[]> res = new Vector<String[]>();
		for (int i=0; i<clauses.size(); ++i) {
			int add = 1;
			String[] curr = clauses.get(i);
			String[] newCurr;
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
	
	public static Vector<String> propagation(Vector<String> clauses) {
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
	
	public static Map<Integer, String> genInput(Vector<String> clauses) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("C:\\Users\\Asus\\shit.txt", "UTF-8");
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<Integer, String> inv_map = new HashMap<Integer, String>();
		
		int cntr = 1;
		ArrayList<String> outp = new ArrayList<String>();
		String nline = "";
		for (int i=0; i<clauses.size(); ++i) {
			nline = "";
			String curr = clauses.get(i);
			String[] rarr = curr.split("\\$");
			for (int j=0; j<rarr.length; ++j) {
				if (map.containsKey(rarr[j].replace("~", ""))) {
					if (rarr[j].contains("~"))
						nline += "-" + map.get(rarr[j].replace("~", "")).toString() + " ";
					else
						nline += map.get(rarr[j].replace("~", "")).toString() + " ";
				}
				else {
					map.put(rarr[j].replace("~", ""), cntr);
					inv_map.put(cntr, rarr[j].replace("~", ""));
					++cntr;
					if (rarr[j].contains("~"))
						nline += "-" + map.get(rarr[j].replace("~", "")).toString() + " ";
					else
						nline += map.get(rarr[j].replace("~", "")).toString() + " ";
				}
			}
			nline += "0";
			outp.add(nline);
		}
		writer.println("p cnf " + String.valueOf(cntr-1) + " " + String.valueOf(clauses.size()));
		for (int i=0; i<outp.size(); ++i)
			writer.println(outp.get(i));
		writer.close();
		return inv_map;
	}
	
	public static String[] parseOutput(String outp, Map<Integer, String> map) {
		String[] liters = outp.split(" ");
		ArrayList<String> res = new ArrayList<String>();
		for (int i=0; i<liters.length; ++i) {
			if (liters[i].contains("-")) {
				res.add(map.get(Integer.parseInt((liters[i].replace("-", ""))))+"=False");
			}
			else
				res.add(map.get(Integer.parseInt(liters[i]))+"=True");
		}
		return res.toArray(new String[res.size()]);
	}
	
	
	public static void main(String args[]) throws IOException {
		Vector<String> v = new Vector<String>();
		  
	    v.add("~True$~False$~True$~False");
	    v.add("a$b$v(0,2,4)$k(2,4)$~c");
	    v.add("~v(0,24)$~v(0,2,4)");
	    v.add("~k(2,4)");
	    v.add("a");
	    v.add("b");
	    //v.add("k(2,4)");
	    /*
		v.add("True");
		v.add("v(5,0)");
		v.add("v(6,10)");
		v.add("v(7,20)");
		v.add("~v(8,30)");
		v.add("v(9,40)");
		v.add("v(10,50)");
		*/
	    v = propagation(v);
	 
	    for(int index=0; index < v.size(); index++)
	      System.out.println(v.get(index));
	    
	    if (v.get(0).equals("False") || v.get(0).equals("True"))
	    	System.out.println(v.get(0));
	    else {
		    Map<Integer, String> map = genInput(v);
			
		try 
	        {
			String cmd = "C:\\Users\\Asus\\Desktop\\z3\\z3 --dimacs C:\\Users\\Asus\\shit.txt > C:\\Users\\Asus\\shit_output.txt";
			Process p=Runtime.getRuntime().exec(cmd); 
	        p.waitFor(); 
	        p.destroy();
	        BufferedReader reader=new BufferedReader(
	            new InputStreamReader(p.getInputStream())
	        );
	        
	        String line; 
	        int i = 0;
	        
	        PrintWriter writer = new PrintWriter("output_z3.txt", "UTF-8");
			while((line = reader.readLine()) != null) 
	        { 
			    writer.println(line);
		        if (i>0) {
	            	String[] wrout = parseOutput(line,map);
	            	for (int j=0; j<wrout.length; ++j) {
	            		System.out.println(wrout[j]);
	            	}
	            }
	            else 
	            	++i;
	        } 
			writer.close();
	    }
	    catch(IOException e1) {} 
	    catch(InterruptedException e2) {} 		
		
		}
	}
}
