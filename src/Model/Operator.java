package Model;

public interface Operator {
	public <T extends Number> double check(Valuable<T> lValue, Valuable<T> rValue);
	public String getSymbol();
}
