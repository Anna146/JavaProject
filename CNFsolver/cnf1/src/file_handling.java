


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
//import java.util.ArrayList;



public class file_handling {
	private String input_dir = ".//src//";
	private String cnf_output_dir = ".//src//cnf_format//";
	private String input_file_name = "input";
	//private String cnf_file_dir = ".//src//cnf_format//";
	private String cnf_file_name = "cnf_format";
	private String model_file_dir = ".//src//generated_models//";
	private String model_file_name = "generated_models";
	
	//map related variables
	private String map_folder = ".//src//maps//";
	private String z3_mapping = "z3_map.txt";
	private String z3_inv_mapping = "inv_z3_mapping.txt";
	
	//z3 related variables
	private String z3_location = ".//z3//z3";
	private String z3_input_dir = ".//src//z3_input//";
	private String z3_output_dir = ".//src//z3_output//";
	private String z3_input_file = "z3_input_file";
	private String z3_output_file = "z3_output_file";
	private String z3_input_format = "--dimacs";
	private String char_encoding = "UTF-8";
	private String file_extension = ".txt";
	
	private String batch_file_location = ".\\";
	private String batch_file_name = "run_batch.bat";
	public Map<String, Integer> map = new HashMap<String, Integer>();
	public Map<Integer, String> inv_map = new HashMap<Integer, String>();
	public Map<String, Integer> z3_map = new HashMap<String,Integer>();
	public int no_of_variables = 0;
	public int no_of_clauses = 0;
	
	//private String 
	public FileInputStream read() throws IOException{
		
		FileInputStream file_content = new FileInputStream(input_dir + input_file_name + file_extension);
        return file_content;
		
	}
	public void write_cnf_human_readable(Vector<String> vec) throws FileNotFoundException
	{
		PrintWriter file_writer = new PrintWriter(cnf_output_dir + cnf_file_name + file_extension);
		for(int i=0;i<vec.size();++i)
		{
			String s = vec.get(0);
			String s1=s.replace("$", ",");
			/*String[] temp = vec.get(0).split("\\$");
			boolean first_turn = true;
			for(int j=0;j<temp.length;++j)
			{
				if(first_turn)
				{
					s = temp[j];
				}
				else
				{
					s = "$" + s;
				}
			}*/
			s1 = "["+s1+"]";
			file_writer.println(s1);
		}
		file_writer.close();
	}
	
	public Vector<String> genInput(Vector<String> clauses) throws FileNotFoundException, UnsupportedEncodingException {
		map.clear();
		inv_map.clear();
		
		int cntr = 1;
		Vector<String> outp = new Vector<String>();
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
		no_of_variables = cntr;
		no_of_clauses = clauses.size();
		if(map.isEmpty() == false)
		{
			try {
				write_z3_map(map,z3_mapping);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(inv_map.isEmpty() == false)
		{
			try {
				write_inv_z3_map(inv_map,z3_inv_mapping);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//write_z3_input_file(outp);
		return outp;
	}

	public String[] parseOutput(String outp) {
		String[] liters = outp.split(" ");
		ArrayList<String> res = new ArrayList<String>();
		for (int i=0; i<liters.length; ++i) {
			if (liters[i].contains("-")) {
				res.add(inv_map.get(Integer.parseInt((liters[i].replace("-", ""))))+" = False");
				z3_map.put(inv_map.get(Integer.parseInt((liters[i].replace("-", "")))), 1);
			}
			else
			{
				res.add(inv_map.get(Integer.parseInt(liters[i]))+" = True");
				z3_map.put(inv_map.get(Integer.parseInt(liters[i])), 1);
			}
				
		}
		return res.toArray(new String[res.size()]);
	}
	
	public Vector<String> call_z3_solver (int index) throws IOException
	{
		//parses
		Vector<String> result = new Vector<String>();
			//Map<Integer, String> map_1 = genInput(v);
			try 
		    {
				String cmd = z3_location + " " + z3_input_format + " " + z3_input_dir + z3_input_file  + "_" + Integer.toString(index)+ file_extension + " > "+ z3_output_dir + z3_output_file + "_" + Integer.toString(index)+ file_extension;
				//String cmd = "C:\\Users\\Asus\\Desktop\\z3\\z3 --dimacs C:\\Users\\Asus\\shit.txt > C:\\Users\\Asus\\shit_output.txt";
				Process p=Runtime.getRuntime().exec(cmd); 
		        p.waitFor();
		        p.destroy();
		        BufferedReader reader=new BufferedReader(
		            new InputStreamReader(p.getInputStream())
		        ); 
		        String line; 
		        int i = 0;
		        
		        z3_map.clear();
		        boolean is_sat = true;
		        PrintWriter writer = new PrintWriter(z3_output_dir + z3_output_file + "_" + Integer.toString(index)+ file_extension, char_encoding);
		        while((line = reader.readLine()) != null) 
		        { 
		            //System.out.println(line);
		            writer.println(line);
		        	if (i>0 && is_sat) {
		            	String[] wrout = parseOutput(line);
		            	//String[] wrout = parseOutput("-2 3 -4",map);
		            	for (int j=0; j<wrout.length; ++j) {
		            		result.add(wrout[j]);
		            		//System.out.println(wrout[j]);
		            	}
		            }
		            else
		            {
		            	//the first line contains if there exists multiple solutions or not
		            	result.add(line);
		            	if(line.contains("unsat"))
		            	{
		            		is_sat = false;
		            	}
		            	++i;
		            }
		            	
		        }
		        writer.close();
		        
				return result;
		    }
		    catch(IOException e1) {
		    	result.add("IOException");
		    	return result;
		    } 
		    catch(InterruptedException e2) {
		    	result.add("InterruptedException");
		    	return result;
		    } 		
			
	}
	
	public void write_z3_input_file(Vector<String> outp, int index) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(z3_input_dir + z3_input_file +"_"+ Integer.toString(index) + file_extension, char_encoding);
		writer.println("p cnf " + String.valueOf(no_of_variables - 1) + " " + String.valueOf(no_of_clauses));
		for (int i=0; i<outp.size(); ++i)
			writer.println(outp.get(i));
		writer.close();
	}
	
	public void write_answer(Vector<String> v1) throws IOException
	{
		FileWriter writer = new FileWriter(model_file_dir+ model_file_name + file_extension, true);
		for (int i=0; i<v1.size(); ++i)
		{
			writer.write(v1.get(i));
			writer.write("\n");
		}
			
		writer.close();
	}
	
	public void delete_files() throws IOException, InterruptedException
	{
		String s = "cmd /c start "+ batch_file_location + batch_file_name;
		Process p = Runtime.getRuntime().exec(s);
		p.destroy();
		//p.waitFor();
	}
	
	public Vector<String> new_input(Vector<String>input_file_array, int index) throws IOException
	{
		Vector<String> v = new Vector<String>();
		v.addAll(input_file_array);
		
		String sCurrentLine;

		BufferedReader br = new BufferedReader(new FileReader(z3_output_dir + z3_output_file + "_" + Integer.toString(index-1)+ file_extension));
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.contains("sat") ||  sCurrentLine.contains("unsat"))
			{
				continue;
			}
			else
			{
				String to_write = new String();
				String[] temp = sCurrentLine.split(" ");
				for(String str : temp)
				{
					if(str.contains("-"))
					{
						to_write += str.replace("-", "") + " ";
					}
					else
					{
						to_write += "-" + str + " ";
					}
				}
				to_write+="0";
				v.add(to_write);
				++no_of_clauses;
			}
		}
		br.close();
		return v;
		
	}
	
	public void write_z3_map(Map<String, Integer> map, String file_name) throws IOException
	{
		FileWriter writer = new FileWriter(map_folder + file_name);
		writer.write("Map from literals to z3 variables \n");
		for(String s:map.keySet())
		{
			writer.write(s + "\t\t\t" + map.get(s));
			writer.write("\n");
		}
			
		//writer.println("\n");
		writer.close();
	}
	public void write_inv_z3_map(Map<Integer, String> map, String file_name) throws IOException
	{
		FileWriter writer = new FileWriter(map_folder + file_name);
		writer.write("Map from z3 variables to literals \n");
		for(Integer s:map.keySet())
		{
			writer.write(s + "\t\t\t" + map.get(s));
			writer.write("\n");
		}
			
		writer.close();
	}
}
