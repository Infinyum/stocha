package Model;

public class Expression implements Valuable {

	protected Valuable lValue;
	protected Valuable rValue;
	protected Operator op;
		
	public Expression(Valuable lValue, Operator op, Valuable rValue) {
		super();
		this.lValue = lValue;
		this.rValue = rValue;
		this.op = op;
	}
	
	public Valuable getlValue() {
		return lValue;
	}

	public void setlValue(Valuable lValue) {
		this.lValue = lValue;
	}

	public Valuable getrValue() {
		return rValue;
	}

	public void setrValue(Valuable rValue) {
		this.rValue = rValue;
	}

	public Operator getOp() {
		return op;
	}

	public void setOp(Operator op) {
		this.op = op;
	}

	@Override
	public Number getValue() {
		return op.check(lValue, rValue);
	}

	@Override
	public String toString() {
		return "(" + lValue.toString() + ")" + op.getSymbol() + "(" + rValue.toString() + ")";
	}

	public static void main(String[] args) {
	
		
		Variable v = new Variable("x",3);
		
		// e = "50-35+2+x"
		Expression e = new Expression(new VariableLessExpression("50-35+2"), ArithmeticOperator.PLUS,v);
		// e2 = "e>=55"
		Expression e2 = new Expression(e,ComparisonOperator.GREATER_EQ,new VariableLessExpression("55"));
		System.out.println(e);
		System.out.println(e.getValue());
		System.out.println(e2.getValue());
		
		v.val=50;
		
		System.out.println("\n-----------------------------------\n\n"+e.getValue());
		System.out.println(e2.getValue());
		
		Expression e3 = new Expression(e,ArithmeticOperator.MULT,new VariableLessExpression("14-23"));
		
		System.out.println(e3);
		
	}

	
}
