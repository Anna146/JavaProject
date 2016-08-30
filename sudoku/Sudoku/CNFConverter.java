package satsolverfrontend;

import java.io.*;
import java.util.*;

public class CNFConverter {
	private Formula formula;
	private HashSet<String> defined;
	private HashMap<String,Integer> newVarMapping;
	private HashMap<Integer,String> newVarInverseMap;
	private HashMap<String,Integer> mapping;
	private HashMap<Integer,String> inverseMap;
	private ArrayList<Formula> bigConjunction;
	private ArrayList<ArrayList<String>> cnf;
	private int counter = 1;
	private int newVarCounter = 1;
	
	public CNFConverter (Formula formula) {
		defined = new HashSet<String> ();
		newVarMapping = new HashMap<String, Integer> ();
		newVarInverseMap = new HashMap<Integer, String> ();
		mapping = new HashMap<String, Integer> ();
		inverseMap = new HashMap<Integer, String> ();
		bigConjunction = new ArrayList<Formula> ();
		cnf = new ArrayList<ArrayList<String>> ();
	}
	
	public static void main (String[] args) throws IOException {
		Formula formula1 = new Formula(true, "p", new Formula[]{});
		Formula formula2 = new Formula(false, "~", new Formula[]{formula1});
		Formula formulaX = new Formula(false, "~", new Formula[]{formula2});
		
		Formula formula3 = new Formula(true, "b", new Formula[]{});
		Formula formula4 = new Formula(true, "a", new Formula[]{});
		Formula formula5 = new Formula(true, "b", new Formula[]{});
		Formula formula6 = new Formula(true, "c", new Formula[]{});
		Formula formula7 = new Formula(false, "~", new Formula[]{formula6});
		Formula formula8 = new Formula(false, "&", new Formula[]{formula2, formula3});
		Formula formula9 = new Formula(false, "&", new Formula[]{formula4, formula5});
		Formula formula10 = new Formula(false, "&", new Formula[]{formula1, formula8});
		Formula formula11 = new Formula(false, "&", new Formula[]{formula9, formula7});
		Formula formula12 = new Formula(false, "|", new Formula[]{formula10, formula11});
		CNFConverter converter = new CNFConverter (formula12);
		converter.generateCNF(formulaX, "Output");
		//converter.printModel("Output.mod", "Output.map", "Output.out");
	}
	
	public void generateCNF(Formula formula, String outputName) throws IOException {
		Formula temp = renaming(formula);
		bigConjunction.add(temp);
		generateCNF(formula, true);
		
		for (int ii = 0; ii < bigConjunction.size(); ii++) {
			//System.out.println(bigConjunction.get(ii));
			bigConjunction.set(ii, removeImplication(bigConjunction.get(ii)));
			//System.out.println(bigConjunction.get(ii));
			bigConjunction.set(ii, convertNNF(bigConjunction.get(ii)));
			//System.out.println(bigConjunction.get(ii));
			bigConjunction.set(ii, convertCNF(bigConjunction.get(ii)));
			//System.out.println(bigConjunction.get(ii));
			map(bigConjunction.get(ii));
		}
		
		for (int ii = 0; ii < bigConjunction.size(); ii++) {
			addCNF(bigConjunction.get(ii));
		}
		
		int oldVariableNum = mapping.keySet().size();
		int newVariableNum = newVarMapping.keySet().size();
		int variableNum = oldVariableNum + newVariableNum;
		
		File humanReadableFile = new File(outputName + ".txt");
		File dimacsFile = new File(outputName + ".cnf");
		File mappingFile = new File(outputName + ".map");
		
		BufferedWriter humanWriter = new BufferedWriter (new FileWriter(humanReadableFile));
		BufferedWriter dimacsWriter = new BufferedWriter (new FileWriter(dimacsFile));
		BufferedWriter mappingWriter = new BufferedWriter (new FileWriter(mappingFile));
		
		for (int ii = 0; ii < cnf.size(); ii++) {
			ArrayList<String> clause = cnf.get(ii);
			for (int jj = 0; jj < clause.size(); jj++) {
				String tempVar = clause.get(jj);
				if(tempVar.length() > 2 && tempVar.substring(0,3).equals("Var")) {
					humanWriter.write("Var_" + newVarMapping.get(tempVar) + " ");
				} else if (tempVar.length() > 3 && tempVar.substring(0,4).equals("-Var")) {
					humanWriter.write("-Var_" + newVarMapping.get(tempVar.substring(1,tempVar.length())) + " ");
				} else {
					humanWriter.write(clause.get(jj) + " ");
				}
			}
			humanWriter.newLine();
		}
		humanWriter.flush();
	
		for (int ii = 0; ii < cnf.size(); ii++) {
			ArrayList<String> clause = cnf.get(ii);
			for (int jj = 0; jj < clause.size(); jj++) {
				String tempVar = clause.get(jj);
				if(tempVar.equals("True") || tempVar.equals("-False")) {
					cnf.remove(ii);
					continue;
				} else if (tempVar.equals("False") || tempVar.equals("-True")){
					clause.remove(jj);
				}
			}
			clause.trimToSize();
		}
		cnf.trimToSize();
		
		dimacsWriter.write("p cnf " + variableNum + " " + cnf.size());
		dimacsWriter.newLine();
		
		for (int ii = 0; ii < cnf.size(); ii++) {
			ArrayList<String> clause = cnf.get(ii);
			for (int jj = 0; jj < clause.size(); jj++) {
				String tempVar = clause.get(jj);
				if(tempVar.length() > 2 && tempVar.substring(0,3).equals("Var")) {
					dimacsWriter.write((newVarMapping.get(tempVar) + oldVariableNum) + " ");
				} else if (tempVar.length() > 3 && tempVar.substring(0,4).equals("-Var")) {
					dimacsWriter.write("-" + (newVarMapping.get(tempVar.substring(1,tempVar.length())) + oldVariableNum) + " ");
				} else if (tempVar.charAt(0) == '-'){
					dimacsWriter.write("-" + mapping.get(tempVar.substring(1,tempVar.length())) + " ");
				} else {
					dimacsWriter.write(mapping.get(tempVar) + " ");
				}
			}
			dimacsWriter.write("0");
			dimacsWriter.newLine();
		}
		dimacsWriter.flush();
		
		for(int ii = 1; ii <= oldVariableNum; ii++) {
			mappingWriter.write(inverseMap.get(ii) + " : " + ii);
			mappingWriter.newLine();
		}
		for(int ii = 1; ii <= newVariableNum; ii++) {
			mappingWriter.write("Var_" + ii + " : " + (ii + oldVariableNum));
			mappingWriter.newLine();
		}
		mappingWriter.flush();
	}
	
	public void printModel (String modelFile, String mappingFile, String outputFile) throws IOException {
		BufferedReader modelReader = new BufferedReader (new FileReader (modelFile));
		BufferedReader mappingReader = new BufferedReader (new FileReader (mappingFile));
		BufferedWriter outputWriter = new BufferedWriter (new FileWriter(new File(outputFile)));
		
		HashMap<Integer, String> variableMap = new HashMap<Integer, String>();
		String line = "";
		while((line = mappingReader.readLine()) != null) {
			String[] split = line.split(" : ");
			variableMap.put(Integer.parseInt(split[1]), split[0]);
		}
		line = modelReader.readLine();
		String[] split = line.split(" ");
		for (int ii = 0; ii < split.length; ii++) {
			if(split[ii].charAt(0) == '-') {
				outputWriter.write("-" + variableMap.get(Integer.parseInt(split[ii].substring(1,split[ii].length()))) + " ");
				outputWriter.newLine();
			} else {
				outputWriter.write(variableMap.get(Integer.parseInt(split[ii])) + " ");
				outputWriter.newLine();
			}
		}
		outputWriter.flush();
	}
	
	public void addCNF (Formula formula) {
		if(formula.isTerm()) {
			ArrayList<String> list = new ArrayList<String> ();
			list.add(formula.getTerm());
			cnf.add(list);
		} else {
			String operator = formula.getOperator();
			Formula[] operand = formula.getOperand();
			if (operator.equals("~")) {
				ArrayList<String> list = new ArrayList<String> ();
				list.add("-" + operand[0].getTerm());
				cnf.add(list);
			} else if (operator.equals("&")) {
				addCNF(operand[0]);
				addCNF(operand[1]);
			} else {
				ArrayList<String> tempList = new ArrayList<String> ();
				buildClause(formula, tempList);
				cnf.add(tempList);
			}
		}
	}
	
	public void buildClause (Formula formula, ArrayList<String> tempList) {
		if(formula.isTerm()) {
			tempList.add(formula.getTerm());
		} else {
			String operator = formula.getOperator();
			Formula[] operand = formula.getOperand();
			if (operator.equals("~")) {
				tempList.add("-" + operand[0].getTerm());
			} else {
				buildClause(operand[0], tempList);
				buildClause(operand[1], tempList);
			}
		}
	}
	
	public void map (Formula formula) {
		if(formula.isTerm()) {
			String term = formula.getTerm();
			if(term.length() > 2 && term.substring(0,3).equals("Var")) {
				if(!newVarMapping.containsKey(term)) {
					newVarInverseMap.put(newVarCounter, term);
					newVarMapping.put(term, newVarCounter++);				
				} else {
					// do nothing
				}
			} else if (term.equals("True") || term.equals("False")){
				
			} else {
                            if(!mapping.containsKey(term)) {
					inverseMap.put(counter, term);
					mapping.put(term, counter++);
				} else {
					// do nothing
				}
                        }
		} else {
			Formula[] operand = formula.getOperand();
			for(int ii = 0; ii < operand.length; ii++) {
				map(operand[ii]);
			}
		}
	}
	
	public Formula convertNNF(Formula formula) {
		if(formula.isTerm()) {
			return formula;
		} else {
			String operator = formula.getOperator();
			Formula[] operand = formula.getOperand();
			if(operator.equals("~")) {
				if(operand[0].isTerm()) {
					return formula;
				} else if (operand[0].getOperator().equals("&")) {
					Formula formulaX = operand[0].getOperand()[0];
					Formula formulaY = operand[0].getOperand()[1];
					Formula formulaLeft  = convertNNF(new Formula(false, "~", new Formula[]{formulaX}));
					Formula formulaRight = convertNNF(new Formula(false, "~", new Formula[]{formulaY}));
					return new Formula(false, "|", new Formula[]{formulaLeft, formulaRight});
				} else if (operand[0].getOperator().equals("|")){
					Formula formulaX = operand[0].getOperand()[0];
					Formula formulaY = operand[0].getOperand()[1];
					Formula formulaLeft  = convertNNF(new Formula(false, "~", new Formula[]{formulaX}));
					Formula formulaRight = convertNNF(new Formula(false, "~", new Formula[]{formulaY}));
					return new Formula(false, "&", new Formula[]{formulaLeft, formulaRight});
				} else {
					return convertNNF(operand[0].getOperand()[0]);
				}
			} else {
				Formula formulaLeft = convertNNF(operand[0]);
				Formula formulaRight = convertNNF(operand[1]);
				return new Formula(false, operator, new Formula[]{formulaLeft, formulaRight});
			}
		}
	}
	
	public Formula convertCNF(Formula formula) {
		if(formula.isTerm()) {
			return formula;
		} else if (formula.getOperator().equals("~")) {
			return formula;
		} else {	
			String operator = formula.getOperator();
			Formula[] operand = formula.getOperand();
			Formula convertLeft = convertCNF(operand[0]);
			Formula convertRight = convertCNF(operand[1]);
			operand[0] = convertLeft;
			operand[1] = convertRight;
			if(operator.equals("|")) {
				if(operand[0].getOperator().equals("&")) {
					Formula formulaX = operand[0].getOperand()[0];
					Formula formulaY = operand[0].getOperand()[1];
					Formula formulaZ1 = new Formula (operand[1]);
					Formula formulaZ2 = new Formula (operand[1]);
					Formula formulaLeft  = new Formula (false, "|", new Formula[]{formulaX, formulaZ1});
					Formula formulaRight = new Formula (false, "|", new Formula[]{formulaY, formulaZ2});
					formulaLeft = convertCNF(formulaLeft);
					formulaRight = convertCNF(formulaRight);
					return convertCNF(new Formula(false, "&", new Formula[]{formulaLeft, formulaRight}));
				} else if (operand[1].getOperator().equals("&")) {
					Formula formulaX = operand[1].getOperand()[0];
					Formula formulaY = operand[1].getOperand()[1];
					Formula formulaZ1 = new Formula (operand[0]);
					Formula formulaZ2 = new Formula (operand[0]);
					Formula formulaLeft  = new Formula (false, "|", new Formula[]{formulaX, formulaZ1});
					Formula formulaRight = new Formula (false, "|", new Formula[]{formulaY, formulaZ2});
					formulaLeft = convertCNF(formulaLeft);
					formulaRight = convertCNF(formulaRight);
					return convertCNF(new Formula(false, "&", new Formula[]{formulaLeft, formulaRight}));
				} else {
					return new Formula(false, "|", new Formula[]{convertLeft, convertRight});
				}
			} else {
				return new Formula(false, "&", new Formula[]{convertLeft, convertRight});
			}
		}
	}
	
	public void generateCNF(Formula formula, boolean positive) {
		if (formula.isTerm()) {
			// do nothing
		} else {
			if (positive) {
				String operator = formula.getOperator();
				Formula[] operand = formula.getOperand();
				//
				Formula leftImplication = renaming(formula);
				if (operator.equals("&")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula rightImplication = new Formula(false, "&", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					defined.add(formula.toString());
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("|")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula rightImplication = new Formula(false, "|", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("<=>")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula rightImplication = new Formula(false, "<=>", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("=>")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula rightImplication = new Formula(false, "=>", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("<=")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula rightImplication = new Formula(false, "<=", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("^")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula rightImplication = new Formula(false, "^", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("?:")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula thirdChild = renaming(operand[2]);
					Formula rightImplication = new Formula(false, "?:", new Formula[]{leftChild, rightChild, thirdChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("~")) {
					generateCNF(operand[0], false);
				}
			} else {
				String operator = formula.getOperator();
				Formula[] operand = formula.getOperand();
				//
				Formula rightImplication = renaming(formula);
				if (operator.equals("&")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula leftImplication = new Formula(false, "&", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					defined.add(formula.toString());
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("|")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula leftImplication = new Formula(false, "|", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("<=>")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula leftImplication = new Formula(false, "<=>", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("=>")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula leftImplication = new Formula(false, "=>", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("<=")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula leftImplication = new Formula(false, "<=", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("^")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula leftImplication = new Formula(false, "^", new Formula[]{leftChild, rightChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("?:")) {
					Formula leftChild = renaming(operand[0]);
					Formula rightChild = renaming(operand[1]);
					Formula thirdChild = renaming(operand[2]);
					Formula leftImplication = new Formula(false, "?:", new Formula[]{leftChild, rightChild, thirdChild});
					bigConjunction.add(new Formula(false, "=>", new Formula[]{leftImplication, rightImplication}));
					if (!defined.contains(operand[0].toString())) {
						generateCNF(operand[0], true);
					}
					if (!defined.contains(operand[1].toString())) {
						generateCNF(operand[1], true);
					}
				} else if (operator.equals("~")) {
					generateCNF(operand[0], true);
				}
			}			
		}
	}
	
	public Formula removeImplication(Formula formula) {
		if (formula.isTerm()) {
			return formula;
		} else {
			String operator = formula.getOperator();
			Formula[] operand = formula.getOperand();
			if (operator.equals("=>")) {
				Formula leftFormula = operand[0];
				Formula rightFormula = operand[1];
				Formula newLeftFormula = removeImplication(leftFormula);
				Formula newRightFormula = removeImplication(rightFormula);
				Formula[] newOperand = new Formula[2];
				newOperand[0] = new Formula(false, "~", new Formula[] {newLeftFormula});
				newOperand[1] = newRightFormula;
				return new Formula(false, "|", newOperand);
			} else if (operator.equals("<=>")) {
				Formula phi = operand[0];
				Formula psi = operand[1];
				Formula newPhi = removeImplication(phi);
				Formula newPsi = removeImplication(psi);
				//
				Formula[] leftImplOperand = new Formula[2];
				leftImplOperand[0] = new Formula(false, "~", new Formula[]{new Formula(newPhi)});
				leftImplOperand[1] = new Formula(newPsi);
				Formula leftImplication = new Formula(false, "|", leftImplOperand);
				//
				Formula[] rightImplOperand = new Formula[2];
				rightImplOperand[0] = new Formula(newPhi);
				rightImplOperand[1] = new Formula(false, "~", new Formula[]{new Formula(newPsi)});
				Formula rightImplication = new Formula(false, "|", rightImplOperand);
				//
				return new Formula(false, "&", new Formula[]{leftImplication, rightImplication});
			} else if (operator.equals("<=")) {
				Formula leftFormula = operand[0];
				Formula rightFormula = operand[1];
				Formula newLeftFormula = removeImplication(leftFormula);
				Formula newRightFormula = removeImplication(rightFormula);
				Formula[] newOperand = new Formula[2];
				newOperand[0] = newLeftFormula;
				newOperand[1] = new Formula(false, "~", new Formula[] {newRightFormula});
				return new Formula(false, "||", newOperand);
			} else if (operator.equals("^")) {
				Formula phi = operand[0];
				Formula psi = operand[1];
				Formula newPhi = removeImplication(phi);
				Formula newPsi = removeImplication(psi);
				//
				Formula[] leftImplOperand = new Formula[2];
				leftImplOperand[0] = new Formula(false, "~", new Formula[]{new Formula(newPhi)});
				leftImplOperand[1] = new Formula(newPsi);
				Formula leftImplication = new Formula(false, "&", leftImplOperand);
				//
				Formula[] rightImplOperand = new Formula[2];
				rightImplOperand[0] = new Formula(newPhi);
				rightImplOperand[1] = new Formula(false, "~", new Formula[]{new Formula(newPsi)});
				Formula rightImplication = new Formula(false, "&", rightImplOperand);
				//
				return new Formula(false, "|", new Formula[]{leftImplication, rightImplication});
			} else if (operator.equals("?:")) {
				Formula phi = operand[0];
				Formula psi = operand[1];
				Formula tau = operand[2];
				Formula newPhi = removeImplication(phi);
				Formula newPsi = removeImplication(psi);
				Formula newTau = removeImplication(tau);
				//
				Formula[] leftImplOperand = new Formula[2];
				leftImplOperand[0] = new Formula(false, "~", new Formula[]{new Formula(newPhi)});
				leftImplOperand[1] = new Formula(newPsi);
				Formula leftImplication = new Formula(false, "|", leftImplOperand);
				//
				Formula[] rightImplOperand = new Formula[2];
				rightImplOperand[0] = new Formula(newPhi);
				rightImplOperand[1] = new Formula(newTau);
				Formula rightImplication = new Formula(false, "|", rightImplOperand);
				//
				return new Formula(false, "&", new Formula[]{leftImplication, rightImplication}); 
			} else {
				Formula[] newOperand = new Formula[operand.length];
				for (int ii = 0; ii < operand.length; ii++) {
					newOperand[ii] = removeImplication(operand[ii]);
				}
				return new Formula (false, operator, newOperand);
			}
		}
	}
	
	public Formula renaming(Formula formula) {
		while(formula.getOperator().equals("~") && formula.getOperand()[0].getOperator().equals("~")) {
			formula = formula.getOperand()[0].getOperand()[0];
		}
		if(formula.isTerm()) {
			return new Formula(true, formula.getTerm(), new Formula[]{});
		} else {
			if(formula.getOperator().equals("~")) {
				if(formula.getOperand()[0].isTerm()) {
					Formula temp = new Formula(true, formula.getOperand()[0].getTerm(), new Formula[]{});
					return new Formula(false, "~", new Formula[]{temp});
				} else {
					Formula temp = new Formula(true, "Var_" + formula.getOperand()[0].toString(), new Formula[]{});
					return new Formula(false, "~", new Formula[]{temp});
				}
			} else {
				return new Formula(true, "Var_" + formula.toString(), new Formula[]{});
			}
		}
	}
}