package Model;

public enum MinMaxEnum {
	MIN,
	MAX;
	
	public String toString() {
		if(this==MIN) {
			return "min";
		}
		else {
			return "max";
		}
	}
}
