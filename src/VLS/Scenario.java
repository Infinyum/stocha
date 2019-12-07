package VLS;

import Model.Recuit;

public class Scenario {
	Integer[][] demandMatrix;
	double probability;
	Recuit recuit;
	
	public Scenario(double probability, Integer[][] demandMatrix) {
		this.probability = probability;
		this.demandMatrix = demandMatrix;
		this.recuit = null;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public Recuit getRecuit() {
		return recuit;
	}

	public void setRecuit(Recuit recuit) {
		this.recuit = recuit;
	}

	public Integer[][] getDemandMatrix() {
		return demandMatrix;
	}
}
