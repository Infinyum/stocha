package VLS;

public class Station {
	int id;
	String name;
	int k;
	
	public Station(int id, String name, int k) {
		super();
		this.id = id;
		this.name = name;
		this.k = k;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", name=" + name + ", k=" + k + "]";
	}
}
