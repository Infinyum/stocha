package Model;

public enum ArithmeticOperator implements Operator{
	PLUS("+"),
	MINUS("-"),
	MULT("*"),
	DIV("/");
	
	public String symbol;
	ArithmeticOperator(String symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public <T extends Number> double check(Valuable<T> lValue, Valuable<T> rValue) {
		
		switch(this) {
			case PLUS:{
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return  v1+v2;
			}
			case MINUS:{
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return  v1-v2;
			}
			case MULT:{
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return  v1*v2;
			}
			case DIV:{
				if(rValue.getValue().doubleValue()==0.0f) {
					return (Double)null;
				}
				double v1 = lValue.getValue().doubleValue();
				double v2 = rValue.getValue().doubleValue();
				return  v1/v2;
			}
			default : return (Double) null;
		}	
	}
	
}
