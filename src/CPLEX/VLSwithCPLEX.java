package CPLEX;

import ilog.concert.*;
import ilog.cplex.*;
import java.util.List;

import VLS.Scenario;
import VLS.Station;

public class VLSwithCPLEX {
	public static void solveMe2(List<Station> stations, List<Scenario> scenarios) {
		
		
		int n = stations.size();
		int S = scenarios.size();
		Integer[][][] E = new Integer[n][n][S];
		
		for (int i = 0 ; i < n ; i++) {
			for (int j = 0 ; j < n ; j++) {
				for (int s = 0 ; s < S ; s++) {
					E[i][j][s] = scenarios.get(s).getDemandMatrix()[i][j];
				}
			}
		}
		
		try {	
			IloCplex cplex = new IloCplex();
			
			// DECISION VARIABLES
			IloNumVar[] x = cplex.numVarArray(n, 0d, Double.MAX_VALUE, IloNumVarType.Float);
			IloNumVar[][] Iplus = new IloNumVar[n][S];
			IloNumVar[][] Ominus = new IloNumVar[n][S];
			IloNumVar[][] Oplus = new IloNumVar[n][S];
			IloNumVar[][][] B = new IloNumVar[n][n][S];
			IloNumVar[][][] Iminus = new IloNumVar[n][n][S];
			
			for (int i = 0 ; i < n ; i++) {
				Iplus[i] = cplex.numVarArray(S, 0d, Double.MAX_VALUE, IloNumVarType.Float);
				Ominus[i] = cplex.numVarArray(S, 0d, Double.MAX_VALUE, IloNumVarType.Float);
				Oplus[i] = cplex.numVarArray(S, 0d, Double.MAX_VALUE, IloNumVarType.Float);
			}
			
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {					
					B[i][j] = cplex.numVarArray(S, 0d, Double.MAX_VALUE, IloNumVarType.Float);
					Iminus[i][j] = cplex.numVarArray(S, 0d, Double.MAX_VALUE, IloNumVarType.Float);
				}
			}
			
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i = 0 ; i < n ; i++) {
				objective.addTerm(stations.get(i).getC(), x[i]);
			}
			for (int s = 0 ; s < S ; s++) {
				double ps = scenarios.get(s).getProbability();
				for (int i = 0 ; i < n ; i++) {
					IloLinearNumExpr expr = cplex.linearNumExpr();
					for (int j = 0 ; j < n ; j++) {
						expr.addTerm(ps * stations.get(i).getV(), Iminus[i][j][s]);
					}
					expr.addTerm(ps * stations.get(i).getW(), Ominus[i][s]);
					objective.add(expr);
				}
			}
			cplex.addMinimize(objective);
			
			// CONSTRAINTS
			for (int i = 0 ; i < n ; i++) {
				cplex.addLe(x[i], stations.get(i).getK()); // 1a
			}
			
			for (int i = 0 ; i < n ; i++) {
				for (int j = 0 ; j < n ; j++) {
					for (int s = 0 ; s < S ; s++) {						
						cplex.addEq(B[i][j][s], cplex.min(E[i][j][s], Iminus[i][j][s])); // 1b
					}
				}
			}
			
			for (int s = 0 ; s < S ; s++) {				
				for (int i = 0 ; i < n ; i++) {
					IloLinearNumExpr expr = cplex.linearNumExpr();
					expr.addTerm(1.0, Iplus[i][s]);
					for (int j = 0 ; j < n ; j++) {
						expr.addTerm(-1.0, Iminus[i][j][s]);
					}
					expr.addTerm(-1.0, x[i]);
					int sum = 0;
					for (int j = 0 ; j < n ; j++) {
						sum += E[i][j][s];
					}
					cplex.addEq(expr, -1*sum); // 1c
				}
			}
			
			for (int s = 0 ; s < S ; s++) {				
				for (int i = 0 ; i < n ; i++) {
					IloLinearNumExpr expr = cplex.linearNumExpr();
					expr.addTerm(1.0, Oplus[i][s]);
					expr.addTerm(-1.0, Ominus[i][s]);
					expr.addTerm(1.0, x[i]);
					for (int j = 0 ; j < n ; j++) {
						expr.addTerm(-1.0, B[i][j][s]);
						expr.addTerm(1.0, B[j][i][s]);
					}
					cplex.addEq(expr, stations.get(i).getK()); // 1d
				}
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
	
	
	public static void solveMe(List<Station> stations, Integer[][] E) {
		
		
		int n = stations.size();
		
		try {	
			IloCplex cplex = new IloCplex();
			
			// DECISION VARIABLES
			IloNumVar[] x = cplex.numVarArray(n, 0d, Double.MAX_VALUE, IloNumVarType.Float);
			IloNumVar[] Iplus = cplex.numVarArray(n, 0d, Double.MAX_VALUE, IloNumVarType.Float);
			IloNumVar[] Ominus = cplex.numVarArray(n, 0d, Double.MAX_VALUE, IloNumVarType.Float);
			IloNumVar[] Oplus = cplex.numVarArray(n, 0d, Double.MAX_VALUE, IloNumVarType.Float);
			IloNumVar[][] B = new IloNumVar[n][];
			IloNumVar[][] Iminus = new IloNumVar[n][];
			
			for (int i = 0 ; i < n ; i++) {
				B[i] = cplex.numVarArray(n, 0d, Double.MAX_VALUE, IloNumVarType.Float);
				Iminus[i] = cplex.numVarArray(n, 0d, Double.MAX_VALUE, IloNumVarType.Float);
			}
			
			IloLinearNumExpr objective = cplex.linearNumExpr();
			for (int i = 0 ; i < n ; i++) {
				objective.addTerm(stations.get(i).getC(), x[i]);
			}
			for (int i = 0 ; i < n ; i++) {
				IloLinearNumExpr expr = cplex.linearNumExpr();
				for (int j = 0 ; j < n ; j++) {
					expr.addTerm(stations.get(i).getV(), Iminus[i][j]);
				}
				expr.addTerm(stations.get(i).getW(), Ominus[i]);
				objective.add(expr);
			}
			cplex.addMinimize(objective);
			
			// CONSTRAINTS
			for (int i = 0 ; i < n ; i++) {
				cplex.addLe(x[i], stations.get(i).getK()); // 1a
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
				cplex.addEq(expr, stations.get(i).getK()); // 1d
			}
			
			/*for (int i = 0 ; i < n ; i++) {
				cplex.addGe(x[i], 1.0); // 1e
			}*/
			
			
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
