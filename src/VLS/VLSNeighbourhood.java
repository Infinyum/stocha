package VLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VLSNeighbourhood {
	public List<Station> stations;

	public VLSNeighbourhood(List<Station> stations) {
		this.stations = stations;
	}
	
	public List<Integer> compute(List<Integer> X) {
		List<Integer> newX = new ArrayList<>();
		Random r = new Random();
		
		int newValue = 0;
		for (int i = 0 ; i < X.size() ; i++) {
			// Add or remove one
			newValue = X.get(i) + (r.nextInt(3)-1);
			if (newValue < 0) {
				newValue = 0;
			}
			newX.add(newValue);
			// Random integer between 1 and the capacity of the bike station
			//newX.add(r.nextInt(this.stations.get(i).getK()+1));
		}
		
		return newX;
	}
}
