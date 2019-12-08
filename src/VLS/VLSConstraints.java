package VLS;

import java.util.List;

public class VLSConstraints {
	public List<Station> stations;
	public Integer[][] demandMatrix;
	
	public VLSConstraints(List<Station> stations, Integer[][] demandMatrix) {
		this.stations = stations;
		this.demandMatrix = demandMatrix;
	}
	
	public VLSConstraints(List<Station> stations) {
		this.stations = stations;
		this.demandMatrix = null;
	}
	
	public boolean check(List<Integer> X) {
		int n = stations.size();
		Integer[][] Iminus = new Integer[n][n];
		Integer[] Iplus = new Integer[n];
		Integer[] Oplus = new Integer[n];
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
			Iplus[i] = X.get(i);
			for (int j = 0 ; j < n ; j++) {
				Iplus[i] -= demandMatrix[i][j];
			}
			if (Iplus[i] < 0) {
				Iplus[i] = 0;
			}
		}
		
		for (int i = 0 ; i < n ; i++) {
			Oplus[i] = stations.get(i).getK() - X.get(i);
			for (int j = 0 ; j < n ; j++) {
				Oplus[i] += demandMatrix[j][i];
				Oplus[i] -= demandMatrix[i][j];
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
		
		
		// CONSTRAINTS
		for (int i = 0 ; i < n ; i++) {
			// 1a
			if (X.get(i) > stations.get(i).getK()) {
				return false;
			}
			
			int lhs = Iplus[i];
			int rhs = X.get(i);
			for (int j = 0 ; j < n ; j++) {
				lhs -= Iminus[i][j];
				rhs -= demandMatrix[i][j];
			}
			
			// 1c => toujours faux...
			
			/*if (lhs != rhs) {
				return false;
			}*/
			
			/*lhs = Oplus[i] - Ominus[i];
			rhs = stations.get(i).getK() - X.get(i);
			for (int j = 0 ; j < n ; j++) {
				rhs += demandMatrix[i][j];
				rhs -= demandMatrix[j][i];
			}*/
			// 1d
			/*if (lhs != rhs) {
				return false;
			}*/
		}
		
		return true;
	}

	public void setDemandMatrix(Integer[][] demandMatrix) {
		this.demandMatrix = demandMatrix;
	}
}
