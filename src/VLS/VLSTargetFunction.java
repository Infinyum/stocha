package VLS;

import java.util.List;

public class VLSTargetFunction {
	public List<Station> stations;
	public Integer[][] demandMatrix;
	List<Double> phi;
	List<Double> lambda;
	List<Integer> Xbarre;
	
	public VLSTargetFunction(List<Station> stations) {
		this.stations = stations;
		this.demandMatrix = null;
		this.phi = null;
		this.lambda = null;
		this.Xbarre = null;
	}
	
	public VLSTargetFunction(List<Station> stations, Integer[][] matrix) {
		this.stations = stations;
		this.demandMatrix = matrix;
	}

	public double compute(List<Integer> X) {
		int n = stations.size();
		Integer[][] Iminus = new Integer[n][n];
		Integer[] Ominus = new Integer[n];
		
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < n ; j++) {
				Iminus[i][j] = (demandMatrix[i][j] - X.get(i) < 0 ? 0 : demandMatrix[i][j] - X.get(i));
				if (Iminus[i][j] < 0) {
					Iminus[i][j] = 0;
				}
			}
		}
		
		for (int i = 0 ; i < n ; i++) {
			Ominus[i] = 0;
			for (int j = 0 ; j < n ; j++) {
				Ominus[i] += demandMatrix[j][i];
				Ominus[i] -= demandMatrix[i][j];
			}
			Ominus[i] += X.get(i) - stations.get(i).getK();
			if (Ominus[i] < 0) {
				Ominus[i] = 0;
			}
		}
		
		double value = 0d;
		for (int i = 0 ; i < n ; i++) {
			value += (stations.get(i).getC() + lambda.get(i) - phi.get(i) * Xbarre.get(i)) * X.get(i);
			value += (0.5d * X.get(i) * X.get(i));
		}
		
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < n ; j++) {
				value += stations.get(i).getV() * Iminus[i][j];
			}
			value += stations.get(i).getW() * Ominus[i];
		}
		return value;
	}
	
	
	public void update(List<Double> newPhi, List<Integer> X) {
		phi = newPhi;
		
		// Updating lambda
		double temp = 0d;
		for (int i = 0 ; i < X.size() ; i++) {
			temp = lambda.get(i) + phi.get(i) * (X.get(i) - Xbarre.get(i));
			lambda.set(i, temp);
		}
	}

	
	public void setDemandMatrix(Integer[][] demandMatrix) {
		this.demandMatrix = demandMatrix;
	}

	public void setPhi(List<Double> phi) {
		this.phi = phi;
	}

	public void setLambda(List<Double> lambda) {
		this.lambda = lambda;
	}

	public void setXbarre(List<Integer> xbarre) {
		Xbarre = xbarre;
	}

	public List<Double> getLambda() {
		return lambda;
	}
}
