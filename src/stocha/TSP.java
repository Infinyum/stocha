package stocha;

import ilog.concert.*;
import ilog.cplex.*;

public class TSP {
	public static void solveMe(int n) {
		// Random data
		double[] xPos = new double[n];
		double[] yPos = new double[n];
		
		for (int i = 0 ; i < n ; i++) {
			xPos[i] = Math.random() * 100;
			yPos[i] = Math.random() * 100;
		}
		
		double[][] c = new double[n][n];
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < n ; j++) {
				c[i][j] = Math.sqrt(Math.pow(xPos[i]-xPos[j], 2) + Math.pow(yPos[i] - yPos[j], 2));
			}
		}
		
		// Model
		try {
			IloCplex cplex = new IloCplex();
			
			// Variables
			IloNumVar[][] x = new IloNumVar[n][];
			for (int i = 0 ; i < n ; i++) {
				x[i] = cplex.boolVarArray(n);
			}
			
			IloNumVar[] u = cplex.numVarArray(n, 0, Double.MAX_VALUE);
			
			// Objective
			IloLinearNumExpr obj = cplex.linearNumExpr();
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					if (i != j) {
						obj.addTerm(c[i][j], x[i][j]);
					}
				}
			}
			cplex.addMinimize(obj);
			
			// Constraints
			for (int j = 0 ; j < n ; j++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (int i = 0 ; i < n ; i++) {
					if (i != j) {
						expr.addTerm(1.0, x[i][j]);
					}
				}
				cplex.addEq(expr, 1.0);
			}
			
			for (int i = 0 ; i < n ; i++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (int j = 0 ; j < n ; j++) {
					if (i != j) {
						expr.addTerm(1.0, x[i][j]);
					}
				}
				cplex.addEq(expr, 1.0);
			}
			
			for (int i = 1 ; i < n ; i++) {
				for (int j = 1 ; j < n ; j++) {
					if (j != i) {
						IloLinearNumExpr expr = cplex.linearNumExpr();
						expr.addTerm(1.0, u[i]);
						expr.addTerm(-1.0, u[j]);
						expr.addTerm(n-1, x[i][j]);
						cplex.addLe(expr, n-2);
					}
				}
			}
			
			cplex.solve();
			
			cplex.end();
		} catch (IloException e) {
			e.printStackTrace();
		}
		
		
	}
}
