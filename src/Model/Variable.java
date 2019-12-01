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
	
	@Override
	public T getValue() {
		return val;
	}

	@Override
	public String toString() {
		return  name + " = " + val;
	}

	public static void main(String[] args) {
		
	}

}
