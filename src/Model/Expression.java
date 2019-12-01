package Model;

public class Expression implements Valuable {

	private Valuable lValue;
	private Valuable rValue;
	private Operator op;
		
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
	public Object getValue() {
		return op.check(lValue, rValue);
	}

	public static void main(String[] args) {
	
		
		Variable v = new Variable("x",3);
		Expression e = new Expression(new VariableLessExpression("50-35+2"), ArithmeticOperator.PLUS,v);
		Expression e2 = new Expression(e,ComparisonOperator.GREATER_EQ,new VariableLessExpression("55"));
		System.out.println(e.getValue());
		System.out.println(e2.getValue());
		
		v.val=50;
		
		System.out.println("\n-----------------------------------\n\n"+e.getValue());
		System.out.println(e2.getValue());
		
	}

	
}
