package Model;

public class Variable<T extends Number> implements Valuable<T>{
	
	//ça n'a pas de sens de masquer les champs de la variable
	public String name;
	public T val;
	
	public Variable(String name) {
		this.name = name;
		this.val = null;
	}
	
	public Variable(String name, T val) {
		this.name = name;
		this.val = val;
	}
	
	public Variable copy() {
		return new Variable(name,val);
	}
	
	@Override
	public T getValue() {
		return val;
	}

	@Override
	public String toString() {
		return  name;
	}

	public static void main(String[] args) {
		
	}

}
