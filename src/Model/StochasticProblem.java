package Model;

import java.util.ArrayList;

public class StochasticProblem {
	
	public TargetFunction f;
	public ArrayList<Constraint> constraints;
	public ArrayList<Variable> firstLevelVariables;
	public ArrayList<Variable> secondLevelVariables;
	public ArrayList<Variable> parameters;
	
	public StochasticProblem(TargetFunction f, ArrayList<Variable> v, ArrayList<Variable> v2,ArrayList<Variable> p) {
		//ici on profite du fait que Java travaille par référence => on ne copie pas les objets
		this.f = f;
		firstLevelVariables = v;
		secondLevelVariables = v2;
		parameters = p;
	}
	
	public StochasticProblem(TargetFunction f, ArrayList<Constraint> c, ArrayList<Variable> v, ArrayList<Variable> v2,ArrayList<Variable> p) {
		//ici on profite du fait que Java travaille par référence => on ne copie pas les objets
		this.f = f;
		constraints = c;
		firstLevelVariables = v;
		secondLevelVariables = v2;
		parameters = p;
	}
	
	public double getTargetFunctionValue() {
		return f.eval();
	}
	
	public boolean verifyConstraints() {

		for(Constraint c : constraints) {
			if( ((Number)(c.getValue())).doubleValue() == 0.0f ) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public String toString() {
		String res = "StochasticProblem : \n\t" + f + "\ns.c." ;
		
		for(Constraint c : constraints) {
			res += "\n\t" + c.toString();
		}
		
		res += "\nVaribles :\n";
		
		for(Variable v : firstLevelVariables) {
			res += v.name + " = " + v.val + ", ";
		}
		
		res = res.substring(0, res.length()-2);
		
		return res;
	}
	
public static void main(String[] args) {
	
		
		Variable v = new Variable("x",6);
		Variable v2 = new Variable("x2",1);
		
		ArrayList<Variable> var = new ArrayList<>();
		var.add(v);
		var.add(v2);
		
		
		// e = "50-35+2+x"
		Expression e = new Expression(new VariableLessExpression("50-35+2"), ArithmeticOperator.PLUS,v);
		e = new Expression(e, ArithmeticOperator.MINUS,v2);
		ArrayList<Constraint> c = new ArrayList<>();
		// e2 = "x>=5"
		Constraint e2 = new Constraint(v,ComparisonOperator.GREATER_EQ,new VariableLessExpression("5"));
		
		Expression e3 = new Expression(v,ArithmeticOperator.PLUS,v2);
		Constraint c2 = new Constraint(e3,ComparisonOperator.LESS_EQ,new VariableLessExpression("70"));
		c.add(e2);
		c.add(c2);
		
		TargetFunction f = new TargetFunction(e,MinMaxEnum.MAX);
		
		StochasticProblem pb = new StochasticProblem(f, c, var, new ArrayList<Variable>(), new ArrayList<Variable>());
		
		System.out.println(pb);
		System.out.println(pb.verifyConstraints());
		
	}
	
	
}
