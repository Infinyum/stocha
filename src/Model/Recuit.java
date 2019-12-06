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
	int nbPaliers;
	VLSTargetFunction objective;
	VLSNeighbourhood neighbourhood;
	VLSConstraints constraints;
	
	public Recuit(double temperature, double coef, double eps, int nbPaliers, VLSTargetFunction obj, VLSNeighbourhood nbr, VLSConstraints cstr) {
		this.temperature = temperature;
		this.coef = coef;
		this.epsilon = eps;
		this.nbPaliers = nbPaliers;
		this.objective = obj;
		this.neighbourhood = nbr;
		this.constraints = cstr;
	}
	
	public List<Integer> solveMe(List<Integer> X) {
		List<Integer> Xmeilleur = X;
		List<Integer> Xprime = new ArrayList<>();
		
		double fmin = this.objective.compute(X);
		double t = this.temperature;
		double delta = 0d;
		double objectiveX = 0d;
		int acceptes = 0, engendres = 0;
		
		do {
			int i = 0;
			while (i < nbPaliers) {
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
						double p = (new Random()).nextDouble();
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
				
		double tauxAcceptation = (acceptes / (double)engendres)*100;
		System.out.println("Taux d'acceptation = " + BigDecimal.valueOf(tauxAcceptation).setScale(1, RoundingMode.HALF_UP).doubleValue());
		System.out.println("Solution finale = " + BigDecimal.valueOf(fmin).setScale(1, RoundingMode.HALF_UP).doubleValue());

		return Xmeilleur;
	}

	public void setObjective(VLSTargetFunction objective) {
		this.objective = objective;
	}

	public void setConstraints(VLSConstraints constraints) {
		this.constraints = constraints;
	}
}
