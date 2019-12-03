package CPLEX;

import java.util.stream.IntStream;

import ilog.concert.*;
import ilog.cplex.*;

public class VLSwithCPLEX {
	public static void solveMe(int n, double[] c, double[] v, double[] w, int[] k, Integer[][] E) {

		try {	
			IloCplex cplex = new IloCplex();
			
			// DECISION VARIABLES
			IloNumVar[] x = cplex.numVarArray(n, 0, Double.MAX_VALUE);
			IloNumVar[] Iplus = cplex.numVarArray(n, 0, Double.MAX_VALUE);
			IloNumVar[] Ominus = cplex.numVarArray(n, 0, Double.MAX_VALUE);
			IloNumVar[] Oplus = cplex.numVarArray(n, 0, Double.MAX_VALUE);
			IloNumVar[][] B = new IloNumVar[n][];
			IloNumVar[][] Iminus = new IloNumVar[n][];
			for (int i = 0 ; i < n ; i++) {
				B[i] = cplex.numVarArray(n, 0, Double.MAX_VALUE);
				Iminus[i] = cplex.numVarArray(n, 0, Double.MAX_VALUE);
			}
			
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i = 0 ; i < n ; i++) {
				objective.addTerm(c[i], x[i]);
			}
			for (int i = 0 ; i < n ; i++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (int j = 0 ; j < n ; j++) {
					expr.addTerm(v[i], Iminus[i][j]);
				}
				expr.addTerm(w[i], Ominus[i]);
				objective.add(expr);
			}
			cplex.addMinimize(objective);
			
			// CONSTRAINTS
			for (int i = 0 ; i < n ; i++) {
				cplex.addLe(x[i], k[i]); // 1a
			}
			
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					cplex.addEq(B[i][j], cplex.min(E[i][j], Iminus[i][j])); // 1b
				}
			}
			
			for (int i = 0 ; i < n ; i++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				expr.addTerm(1.0, Iplus[i]);
				for (int j = 0 ; j < n ; j++) {
					expr.addTerm(-1.0, Iminus[i][j]);
				}
				expr.addTerm(-1.0, x[i]);
				int sum = 0;
				for (int j = 0 ; j < n ; j++) {
					sum += E[i][j];
				}
				cplex.addEq(expr, -1*sum); // 1c
			}
			
			for (int i = 0 ; i < n ; i++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				expr.addTerm(1.0, Oplus[i]);
				expr.addTerm(-1.0, Ominus[i]);
				expr.addTerm(1.0, x[i]);
				for (int j = 0 ; j < n ; j++) {
					expr.addTerm(-1.0, B[i][j]);
					expr.addTerm(1.0, B[j][i]);
				}
				cplex.addEq(expr, k[i]); // 1d
			}
			
			for (int i = 0 ; i < n ; i++) {
				cplex.addGe(x[i], 1.0); // 1e
			}
			
			
			// SOLVE AND DISPLAY
			cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);
			
			if (cplex.solve()) {
				System.out.println("solution with objective = " + cplex.getObjValue());
				System.out.print("x = [");
				for (int i = 0 ; i < n ; i++) {					
					System.out.print((int)cplex.getValue(x[i]) + (i == (n-1) ? "" : ","));
				}
				System.out.println("]");
				
			}
			else {
				System.out.println("Model not solved");
			}
			
			cplex.end();
		}
		catch (IloException e) {
			e.printStackTrace();
		}
	}
}
