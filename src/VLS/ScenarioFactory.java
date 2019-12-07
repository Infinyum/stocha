package VLS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScenarioFactory {
	List<Scenario> scenarios;

	public ScenarioFactory(int S, List<Station> stations) {
		this.scenarios = new ArrayList<>();
		
		// Generate S random numbers whose sum is equal to 1
		List<Double> probabilities = n_random(1d, S);
		
		int n = stations.size();
		Random r = new Random();
		// Generate a random demand matrix for each scenario
		for (int s = 0 ; s < S ; s++) {
			Integer[][] demand = new Integer[n][n];
			
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					// Diagonal values equal to 0
					if (i == j) {
						demand[i][j] = 0;
					}
					// Generate a random value between 0 and twice the total capacity of the bike station 
					else {
						demand[i][j] = r.nextInt(2*stations.get(j).getK() + 1); 
					}
				}
			}
			// At this point the demand matrix is complete
			scenarios.add(new Scenario(probabilities.get(s), demand));
		}
	}
	
	public static List<Double> n_random(double targetSum, int numberOfDraws) {
	    Random r = new Random();
	    List<Double> generatedNumbers = new ArrayList<>();

	    // Random numbers
	    double sum = 0d;
	    for (int i = 0; i < numberOfDraws; i++) {
	        double next = r.nextDouble() * targetSum;
	        generatedNumbers.add(next);
	        sum += next;
	    }

	    // Scale to the desired target sum
	    double scale = 1d * targetSum / sum;
	    sum = 0d;
	    for (int i = 0; i < numberOfDraws; i++) {
	    	generatedNumbers.set(i, (double) (generatedNumbers.get(i) * scale));
	        sum += generatedNumbers.get(i);
	    }
	    
	    return generatedNumbers;
	}

	public List<Scenario> getScenarios() {
		return scenarios;
	}
}
