package Model;

public enum ComparisonOperator implements Operator{
	GREATER(">"),
	GREATER_EQ(">="),
	LESS("<"),
	LESS_EQ("<="),
	EQUALS("=");
	
	public String symbol;
	ComparisonOperator(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public <T extends Number> double check(Valuable<T> lValue, Valuable<T> rValue) {
		
		switch(this) {
			case GREATER:{
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return (v1>v2) ? 1.0f:0.0f;
			}
			case GREATER_EQ:{
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return (v1>=v2) ? 1.0f:0.0f;
			}
			case LESS:{
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return (v1<v2) ? 1.0f:0.0f;
			}
			case LESS_EQ:{
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return (v1<=v2) ? 1.0f:0.0f;
			}
			case EQUALS:{
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return (v1==v2) ? 1.0f:0.0f;
			}
			default : return 0.0f;
		}	
	}
	
}
