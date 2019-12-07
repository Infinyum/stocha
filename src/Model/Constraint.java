package Model;

//The only difference between a constraint and an expression is the fact that the constraint has a ComparisonOperator
public class Constraint extends Expression {

	public Constraint(Valuable lValue, ComparisonOperator op, Valuable rValue) {
		super(lValue, op, rValue);
	}
	
	@Override
	public void setOp(Operator op) {
		
		if(op.getClass().getName()=="ComparisonOperator") {
			this.op = op;
		}
		else {
			System.err.println("Invalid Operator " + op + " for a constraint !");
		}
		
	}
}
