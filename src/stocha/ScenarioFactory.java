package stocha;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScenarioFactory {
	Map<Double, int[][]> scenarios;

	public ScenarioFactory(int S, int n) {
		// Generate S random numbers whose sum is equal to 1
		List<Double> mylist = n_random(1d, S);

		for (int i = 0 ; i < n ; i++) {
			// Generate a random int[n][n] for each scenario
			
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

	    System.out.println("Random arraylist " + generatedNumbers);
	    System.out.println("Sum is "+ sum);
	    
	    return generatedNumbers;
	}
	
}
