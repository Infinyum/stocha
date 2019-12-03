package VLS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScenarioFactory {
	Map<Double, Integer[][]> scenarios;

	public ScenarioFactory(int S, int n, int[] k) {
		this.scenarios = new HashMap<Double, Integer[][]>();
		// Generate S random numbers whose sum is equal to 1
		List<Double> probabilities = n_random(1d, S);
		
		// Generate a random demand matrix for each scenario
		for (int s = 0 ; s < S ; s++) {
			Integer[][] demand = new Integer[n][n];
			Random r = new Random();
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					// Diagonal values equal to 0
					if (i == j) {
						demand[i][j] = 0;
					}
					else {
						// Generate a random value between 0 and twice the total capacity of the bike station 
						demand[i][j] = r.nextInt(2*k[j]); 
					}
				}
			}
			// At this point the demand matrix is complete
			scenarios.put(probabilities.get(s), demand);
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

	public Map<Double, Integer[][]> getScenarios() {
		return scenarios;
	}

	@Override
	public String toString() {
		String s = new String("Scenarios:\n");
		for (Map.Entry<Double, Integer[][]> entry : scenarios.entrySet()) {
			s += "Probability: " + entry.getKey() + "\nDemand matrix:\n" + Arrays.deepToString(entry.getValue()) + "\n\n";
		}
		return s;
	}	
}
