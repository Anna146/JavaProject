import java.util.Hashtable;
import java.util.Vector;

public class Tree_Visitor extends FormulaBaseVisitor<Formula> {
	public Hashtable<String,Integer> tokens_hash= new Hashtable<String,Integer>();
	public static Vector<String> var_vector = new Vector<String>();
	public static Vector<String> var_value_vector = new Vector<String>();
	//public static Vector<String> var_string_vector = new Vector<String>();
	//public static boolean turn =false;
	@Override
	public Formula visitFormula(FormulaParser.FormulaContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		for(int i=0;i<ctx.expression().size();++i)
		{
			 f.clause_vector.addAll(visit(ctx.expression(i)).clause_vector);
		}
		f.type = "formula";
		//return f;
		return f.optimize(f);
	}

	@Override
	public Formula visitDoThen(FormulaParser.DoThenContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.expression(0));
		Formula f2 = visit(ctx.expression(1));
		Formula f3 = f1.do_implication(f1, f2);
		f3.type = "expression";
		//f3 = f3.clean_clauses(f3);
		//return f3;
		return f3.optimize(f3);
	}

	@Override
	public Formula visitDoOr(FormulaParser.DoOrContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.expression(0));
		Formula f2 = visit(ctx.expression(1));
		Formula f3 = f1.do_disjunction(f1, f2);
		//f3 = f3.clean_clauses(f3);
		//return f3;
		f3.type = "exression";
		return f3.optimize(f3);
	}

	@Override
	public Formula visitDoXor(FormulaParser.DoXorContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.expression(0));
		Formula f2 = visit(ctx.expression(0));
		Formula f3 = f1.do_negation(f1);
		Formula f4 = f2.do_negation(f2);
		Formula f5 = f3.do_conjunction(f3, f2);
		Formula f6 = f4.do_conjunction(f4,f1);
		Formula f7 = f6.do_disjunction(f5, f6);
		//f7 = f7.clean_clauses(f7);
		//return f7;
		f7.type = "expression";
		return f7.optimize(f7);
	}

	@Override
	public Formula visitNewIdentifier(FormulaParser.NewIdentifierContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		f.type = "expression";
		String s = ctx.getText();
		f.clause_vector.add(s);
		if(tokens_hash.containsKey(s) == false)
		{
			tokens_hash.put(s, 1);
		}
		else
		{
			int value = tokens_hash.get(s);
			tokens_hash.replace(s, value+1);
		}
		return f;
	}

	@Override
	public Formula visitDoIF(FormulaParser.DoIFContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.expression(0));
		Formula f2 = visit(ctx.expression(1));
		Formula f3 = f1.do_implication(f2, f1);
		//f3 = f3.clean_clauses(f3);
		//return f3;
		f3.type = "expression";
		return f3.optimize(f3);
	}

	@Override
	public Formula visitDoPredicate(FormulaParser.DoPredicateContext ctx) {
		// TODO Auto-generated method stub
		String s= ctx.ID().getText() +"(" ;
		Formula f1 = visit(ctx.term(0));
		s+=f1.clause_vector.get(0);
		for(int i=1;i<ctx.term().size();++i)
		{
			Formula f2 = visit(ctx.term(i));
			s+="," + f2.clause_vector.get(0);
		}
		s+=")";
		if(tokens_hash.containsKey(s) == false)
		{
			tokens_hash.put(s, 1);
		}
		else
		{
			int value = tokens_hash.get(s);
			tokens_hash.replace(s, value+1);
		}
		Formula f = new Formula();
		f.type = "expression";
		f.clause_vector.add(s);
		return f;
		//return f.clean_clauses(f);
	}

	@Override
	public Formula visitDoExpandOr(FormulaParser.DoExpandOrContext ctx) {
		// TODO Auto-generated method stub
		String var = (ctx.VAR().getText());
		Formula f_range = new Formula();
		Formula f_set = new Formula();
		try
		{
			 f_range = visit(ctx.range_expr());
		} catch (NullPointerException e)
		{
			f_range.type = "no";
			try
			{
				 f_set = visit(ctx.set_expr());
			} catch (NullPointerException e1)
			{
				f_set.type = "no";
			}
		}
		if( f_range.type.equals("no"))
		{
			//code for set expression
			//Formula f = new Formula();
			//f = visit(ctx.set_expr());
			boolean needs_removal = false;
			if(!var_vector.contains(var))
			{
				var_vector.add(var);
				needs_removal = true;
			}
			int size = f_set.clause_vector.size();
			Vector<String> s = new Vector<String>();
			s.add("False");
			Formula f1 = new Formula("expression",s);
			for(int i=0;i<size;++i)
			{
				if(var_value_vector.size() <= var_vector.indexOf(var))
				{
					//turn = false;
					var_value_vector.add(var_vector.indexOf(var),f_set.clause_vector.get(i));
				}
				else
				{
					//turn = false;
					var_value_vector.set(var_vector.indexOf(var), f_set.clause_vector.get(i));
				}
				Formula temp = visit(ctx.expression());
				f1=f1.do_disjunction(f1, temp);
			}
			if(needs_removal)
			{
				var_value_vector.remove(var_vector.indexOf(var)) ;
				var_vector.remove(var_vector.indexOf(var));
			}
			//turn = true;
			//return f1;
			return f1.optimize(f1);
		}
		else
		{
			//code for range expression
			//Formula f = new Formula();
			//f = visit(ctx.range_expr());
			boolean needs_removal = false;
			if(!var_vector.contains(var))
			{
				var_vector.add(var);
				needs_removal = true;
			}
			
			long start=Long.parseLong(f_range.clause_vector.get(0));
			long end=Long.parseLong(f_range.clause_vector.get(1));
			Vector<String> s = new Vector<String>();
			s.add("False");
			Formula f1 = new Formula("expression",s);
			for(long i=start;i<=end;++i)
			{
				if(var_value_vector.size() <= var_vector.indexOf(var))
				{
					var_value_vector.add(var_vector.indexOf(var), Long.toString(i));
					//turn = true;
				}
				else
				{
					//turn = true;
					var_value_vector.set(var_vector.indexOf(var), Long.toString(i));
				}
				//var_value_vector.add(var_vector.indexOf(var), i);
				Formula temp = visit(ctx.expression());
				f1=f1.do_disjunction(f1, temp);
				try
				{
					i = Long.parseLong(var_value_vector.get(var_vector.indexOf(var)));
				}
				catch (NumberFormatException e)
				{
					break;
				}
				
			}
			//turn = false;
			if(needs_removal)
			{
				var_value_vector.remove(var_vector.indexOf(var)) ;
				var_vector.remove(var_vector.indexOf(var));
			}
			//return f1;
			return f1.optimize(f1);
		}
	}
	

	@Override
	public Formula visitNewFalseBoolean(FormulaParser.NewFalseBooleanContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		f.type = "expression";
		f.clause_vector.add(ctx.getText());
		return f;
	}

	@Override
	public Formula visitDoIFF(FormulaParser.DoIFFContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.expression(0));
		Formula f2 = visit(ctx.expression(1));
		Formula f3 = f1.do_implication(f2, f1);
		Formula f4 = f2.do_implication(f1, f2);
		f4 = f4.do_conjunction(f3, f4);
		//f4 = f4.clean_clauses(f4);
		//return f4;
		f4.type = "expression";
		return f4.optimize(f4);
	}

	@Override
	public Formula visitNewVariable(FormulaParser.NewVariableContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		if(var_vector.contains(ctx.getText()))
		{
			//if(turn)
			//{
				f.clause_vector.add(var_value_vector.get(var_vector.indexOf(ctx.getText())));
			//}
			//else
			//{
				//f.clause_vector.add(var_string_vector.get(var_vector.indexOf(ctx.getText())));
			//}
		}
		else
		{
			f.clause_vector.add("False");
		}
		//Formula f1 = new Formula();
		f.type = "expression";
		//f1.clause_vector.addElement(ctx.getText());
		return f;
	}

	@Override
	public Formula visitDoParenthesis(FormulaParser.DoParenthesisContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		f=visit(ctx.expression());
		f.type = "expression";
		return f.optimize(f);
		//return f;
	}

	@Override
	public Formula visitNewTrueBoolean(FormulaParser.NewTrueBooleanContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		f.type = "expression";
		f.clause_vector.add(ctx.getText());
		return f;
	}

	@Override
	public Formula visitDoAnd(FormulaParser.DoAndContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.expression(0));
		Formula f2 = visit(ctx.expression(1));
		Formula f3 = f1.do_conjunction(f1,f2);
		//f3 = f3.clean_clauses(f3);
		//return f3;
		f3.type = "expression";
		return f3.optimize(f3);
	}

	@Override
	public Formula visitDoExpandAnd(FormulaParser.DoExpandAndContext ctx) {
		// TODO Auto-generated method stub
		String var = (ctx.VAR().getText());
		Formula f_range = new Formula();
		Formula f_set = new Formula();
		try
		{
			 f_range = visit(ctx.range_expr());
		} catch (NullPointerException e)
		{
			f_range.type = "no";
			try
			{
				 f_set = visit(ctx.set_expr());
			} catch (NullPointerException e1)
			{
				f_set.type = "no";
			}
		}
		if( f_range.type.equals("no"))
		{
			//code for set expression
			//Formula f = new Formula();
			//f = visit(ctx.set_expr());
			boolean needs_removal = false;
			if(!var_vector.contains(var))
			{
				var_vector.add(var);
				needs_removal = true;
			}
			int size = f_set.clause_vector.size();
			Vector<String> s = new Vector<String>();
			s.add("True");
			Formula f1 = new Formula("expression",s);
			for(int i=0;i<size;++i)
			{
				if(var_value_vector.size() <= var_vector.indexOf(var))
				{
					//turn = false;
					var_value_vector.add(var_vector.indexOf(var),f_set.clause_vector.get(i));
				}
				else
				{
					//turn = false;
					var_value_vector.set(var_vector.indexOf(var), f_set.clause_vector.get(i));
				}
				Formula temp = visit(ctx.expression());
				f1=f1.do_conjunction(f1, temp);
			}
			if(needs_removal)
			{
				var_value_vector.remove(var_vector.indexOf(var)) ;
				var_vector.remove(var_vector.indexOf(var));
			}
			//turn = true;
			//return f1;
			return f1.optimize(f1);
		}
		else
		{
			//code for range expression
			//Formula f = new Formula();
			//f = visit(ctx.range_expr());
			boolean needs_removal = false;
			if(!var_vector.contains(var))
			{
				var_vector.add(var);
				needs_removal = true;
			}
			
			long start=Long.parseLong(f_range.clause_vector.get(0));
			long end=Long.parseLong(f_range.clause_vector.get(1));
			Vector<String> s = new Vector<String>();
			s.add("True");
			Formula f1 = new Formula("expression",s);
			for(long i=start;i<=end;++i)
			{
				if(var_value_vector.size() <= var_vector.indexOf(var))
				{
					var_value_vector.add(var_vector.indexOf(var), Long.toString(i));
					//turn = true;
				}
				else
				{
					//turn = true;
					var_value_vector.set(var_vector.indexOf(var), Long.toString(i));
				}
				//var_value_vector.add(var_vector.indexOf(var), i);
				Formula temp = visit(ctx.expression());
				f1=f1.do_conjunction(f1, temp);
				try
				{
					i = Long.parseLong(var_value_vector.get(var_vector.indexOf(var)));
				}
				catch (NumberFormatException e)
				{
					break;
				}
				
			}
			//turn = false;
			if(needs_removal)
			{
				var_value_vector.remove(var_vector.indexOf(var)) ;
				var_vector.remove(var_vector.indexOf(var));
			}
			//return f1;
			return f1.optimize(f1);
		}
	}

	@Override
	public Formula visitDoITE(FormulaParser.DoITEContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.expression(0));
		Formula f2 = visit(ctx.expression(1));
		Formula f3 = visit(ctx.expression(1));
		Formula f4 = f1.do_negation(f1);
		Formula f5 = f1.do_implication(f1, f2);
		Formula f6 = f4.do_implication(f4,f3);
		Formula f7 = f5.do_conjunction(f5, f6);
		//f7 = f7.clean_clauses(f7);
		//return f7;
		f7.type = "expression";
		return f7.optimize(f7);
	}

	@Override
	public Formula visitDoNot(FormulaParser.DoNotContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.expression());
		f1 = f1.do_negation(f1);
		//f1 = f1.clean_clauses(f1);
		//return f1;
		f1.type = "expression";
		return f1.optimize(f1);
	}

	@Override
	public Formula visitNewTermID(FormulaParser.NewTermIDContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = new Formula();
		f1.type = "term";
		f1.clause_vector.add(ctx.getText());
		return f1;
	}

	@Override
	public Formula visitNewTermVar(FormulaParser.NewTermVarContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		if(var_vector.contains(ctx.getText()))
		{
			//if(turn)
			//{
				f.clause_vector.add(var_value_vector.get(var_vector.indexOf(ctx.getText())));
			//}
			//else
			//{
				//f.clause_vector.add(var_string_vector.get(var_vector.indexOf(ctx.getText())));
			//}
		}
		else
		{
			f.clause_vector.add("0");
		}
		//Formula f1 = new Formula();
		f.type = "term";
		//f1.clause_vector.add(ctx.getText());
		return f;
	}

	@Override
	public Formula visitDoIntExpr(FormulaParser.DoIntExprContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.int_expr());
		f1.type = "term";
		return f1;
	}

	@Override
	public Formula visitUnaryMinusExpression(FormulaParser.UnaryMinusExpressionContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.int_expr());
		Formula f3 = new Formula();
		if(f1.type.equals("intvariable"))
		{
			f3.clause_vector.add("-"+f1.clause_vector.get(0));
			f3.type = "intvariable";
		}
		else
		{
			int x = Integer.parseInt(f1.clause_vector.get(0));
			x = (0-x);
			f3.clause_vector.add(Integer.toString(x));
			f3.type = "intexpression";
		}
		return f3;
	}

	@Override
	public Formula visitNewIntVariable(FormulaParser.NewIntVariableContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		if(var_vector.contains(ctx.getText()))
		{
			//if(turn)
			//{
				f.clause_vector.add(var_value_vector.get(var_vector.indexOf(ctx.getText())));
			//}
			//else
			//{
				//f.clause_vector.add(var_string_vector.get(var_vector.indexOf(ctx.getText())));
			//}
			//f.clause_vector.add(Long.toString(var_value_vector.get(var_vector.indexOf(ctx.getText()))));
			f.type = "intexpression";
		}
		else
		{
			f.clause_vector.add("0");
			f.type = "intvariable";
		}
		//f.clause_vector.add(ctx.getText());
		
		return f;
	}

	@Override
	public Formula visitParenthesizedIntExpression(FormulaParser.ParenthesizedIntExpressionContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.int_expr());
		Formula f3 = new Formula();
		if(f1.type.equals("intvariable"))
		{
			f3.clause_vector.add("("+f1.clause_vector.get(0)+")");
			f3.type = "intvariable";
		}
		else
		{
			f3.clause_vector = f1.clause_vector;
			f3.type = "intexpression";
		}
		return f3;
	}

	@Override
	public Formula visitAbsValueExpression(FormulaParser.AbsValueExpressionContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.int_expr());
		Formula f3 = new Formula();
		if(f1.type.equals("intvariable"))
		{
			f3.clause_vector.add("|"+f1.clause_vector.get(0)+"|");
			f3.type = "intvariable";
		}
		else
		{
			int x = Integer.parseInt(f1.clause_vector.get(0));
			x = Math.abs(x);
			f3.clause_vector.add(Integer.toString(x));
			f3.type = "intexpression";
		}
		return f3;
	}

	@Override
	public Formula visitAdditiveExpression(FormulaParser.AdditiveExpressionContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.int_expr(0));
		String operator = new String(ctx.op.getText());
		Formula f2 = visit(ctx.int_expr(1));
		Formula f3 = new Formula();
		if(f1.type.equals("intvariable") || f2.type.equals("intvariable"))
		{
			f3.clause_vector.add(f1.clause_vector.get(0)+operator+f2.clause_vector.get(0));
			f3.type = "intvariable";
		}
		else
		{
			if(operator.equals("+"))
			{
				int x = Integer.parseInt(f1.clause_vector.get(0));
				int y = Integer.parseInt(f2.clause_vector.get(0));
				long a = x + y;
				f3.clause_vector.add(Long.toString(a));
				f3.type = "intexpression";
			}
			else if(operator.equals("-"))
			{
				int x = Integer.parseInt(f1.clause_vector.get(0));
				int y = Integer.parseInt(f2.clause_vector.get(0));
				int a = x - y;
				f3.clause_vector.add(Integer.toString(a));
				f3.type = "intexpression";
			}
		}
		
		return f3;
	}

	@Override
	public Formula visitNewInteger(FormulaParser.NewIntegerContext ctx) {
		// TODO Auto-generated method stub
		Formula f = new Formula();
		f.clause_vector.add(ctx.getText());
		f.type = "number";
		return f;
	}

	@Override
	public Formula visitMultiplicativeExpression(FormulaParser.MultiplicativeExpressionContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.int_expr(0));
		String operator = new String(ctx.op.getText());
		Formula f2 = visit(ctx.int_expr(1));
		Formula f3 = new Formula();
		if(f1.type.equals("intvariable") || f2.type.equals("intvariable"))
		{
			f3.clause_vector.add(f1.clause_vector.get(0)+operator+f2.clause_vector.get(0));
			f3.type = "intvariable";
		}
		else
		{
			if(operator.equals("*"))
			{
				int x = Integer.parseInt(f1.clause_vector.get(0));
				int y = Integer.parseInt(f2.clause_vector.get(0));
				long a = x * y;
				f3.clause_vector.add(Long.toString(a));
				f3.type = "intexpression";
			}
			else if(operator.equals("/"))
			{
				int x = Integer.parseInt(f1.clause_vector.get(0));
				int y = Integer.parseInt(f2.clause_vector.get(0));
				int a = x/y;
				f3.clause_vector.add(Integer.toString(a));
				f3.type = "intexpression";
			}
			else if(operator.equals("%"))
			{
				int x = Integer.parseInt(f1.clause_vector.get(0));
				int y = Integer.parseInt(f2.clause_vector.get(0));
				int a = x%y;
				f3.clause_vector.add(Integer.toString(a));
				f3.type = "intexpression";
			}
		}
		
		return f3;
	}

	@Override
	public Formula visitRange_expr(FormulaParser.Range_exprContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.int_expr(0));
		Formula f2 = visit(ctx.int_expr(1));
		Formula f3 = new Formula();
		f3.clause_vector.add(f1.clause_vector.get(0));
		f3.clause_vector.add(f2.clause_vector.get(0));
		f3.type = "rangeexpr";
		return f3;
	}

	@Override
	public Formula visitSet_expr(FormulaParser.Set_exprContext ctx) {
		// TODO Auto-generated method stub
		Formula f1 = visit(ctx.term(0));
		for(int i=1;i<ctx.term().size();++i)
		{
			Formula f2 = visit(ctx.term(i));
			f1.clause_vector.add(f2.clause_vector.get(0));
		}
		f1.type = "setexpr";
		return f1;
	}

}
