package Model;

public class TargetFunction {
	private Expression e;
	private MinMaxEnum minMax;
	
	public TargetFunction(Expression e, MinMaxEnum minMax) {
		super();
		this.e = e;
		this.minMax = minMax;
	}
	
	public double eval() {
		return e.getValue().doubleValue();
	}

	@Override
	public String toString() {
		return minMax.name() + " " + e.toString() ;
	}
	
}
