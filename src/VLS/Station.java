package VLS;

import java.util.Random;

public class Station {
	int id;
	String name;
	int k;
	double c;
	double v;
	double w;
	
	public Station(int id, String name, int k) {
		super();
		this.id = id;
		this.name = name;
		this.k = k;
		
		// Generate a random double between 0 and 2.0
		Random r = new Random();
		this.c = r.nextDouble() * 2d;
		this.v = r.nextDouble() * 2d;
		this.w = r.nextDouble() * 2d;
	}

	@Override
	public String toString() {
		return "Station [id=" + id + ", name=" + name + ", k=" + k + ", c=" + c + ", v=" + v + ", w=" + w + "]";
	}

	public int getK() {
		return k;
	}

	public double getC() {
		return c;
	}
	public double getV() {
		return v;
	}

	public double getW() {
		return w;
	}

	public void setC(double c) {
		this.c = c;
	}

	public void setV(double v) {
		this.v = v;
	}

	public void setW(double w) {
		this.w = w;
	}

	
	
}
