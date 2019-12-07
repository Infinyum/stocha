package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import VLS.VLSConstraints;
import VLS.VLSNeighbourhood;
import VLS.VLSTargetFunction;

public class Recuit {
	double temperature;
	double coef;
	double epsilon;
	int nbIter;
	List<Integer> X;
	Double objectiveValue;
	VLSTargetFunction objective;
	VLSNeighbourhood neighbourhood;
	VLSConstraints constraints;
	
	public Recuit(double temperature, double coef, double eps, int nbIter, VLSTargetFunction obj, VLSNeighbourhood nbr, VLSConstraints cstr, List<Integer> Xinit) {
		this.temperature = temperature;
		this.coef = coef;
		this.epsilon = eps;
		this.nbIter = nbIter;
		this.objective = obj;
		this.neighbourhood = nbr;
		this.constraints = cstr;
		this.X = Xinit;
		objectiveValue = null;
	}
	
	public void solveMe() {
		List<Integer> Xmeilleur = this.X;
		List<Integer> Xprime = new ArrayList<>();
		
		double fmin = this.objective.compute(Xmeilleur);
		double t = this.temperature;
		double delta = 0d;
		double objectiveX = 0d;
		int acceptes = 0, engendres = 0;
		Random r = new Random();
		
		do {
			int i = 0;
			while (i < nbIter) {
				objectiveX = this.objective.compute(X);
				Xprime = neighbourhood.compute(X);
				
				// Skip this iteration if Xprime doesn't satisfy the constraints
				if (constraints.check(Xprime)) {
					delta = this.objective.compute(Xprime) - objectiveX;
					
					if (delta < 0) {
						X = Xprime;
						if (objectiveX < fmin) {
							fmin = objectiveX;
							Xmeilleur = X;
						}
					}
					else {
						engendres++;
						double p = r.nextDouble();
						if (p <= Math.exp(-1*delta/t)) {
							acceptes++;
							X = Xprime;
						}
					}
				}
				
				i++;
			}
			
			t = this.coef * t;
			
		} while (t > epsilon);
		
		//double tauxAcceptation = (acceptes / (double)engendres)*100;
		//System.out.println("Taux d'acceptation = " + BigDecimal.valueOf(tauxAcceptation).setScale(1, RoundingMode.HALF_UP).doubleValue());
		//System.out.println("Solution finale = " + BigDecimal.valueOf(fmin).setScale(1, RoundingMode.HALF_UP).doubleValue());
		//System.out.println(Xmeilleur);
		
		this.X = Xmeilleur;
		this.objectiveValue = fmin;
	}

	public VLSTargetFunction getObjective() {
		return objective;
	}

	public List<Integer> getSolution() {
		return X;
	}

	public Double getObjectiveValue() {
		return objectiveValue;
	}
}
