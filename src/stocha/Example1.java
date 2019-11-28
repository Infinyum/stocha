package stocha;

import java.util.ArrayList;
import java.util.List;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

public class Example1 {
	public static void solveMe() {
		try	{
			IloCplex cplex = new IloCplex();
			
			// Variables
			IloNumVar x = cplex.numVar(0, Double.MAX_VALUE, "x");
			IloNumVar y = cplex.numVar(0, Double.MAX_VALUE, "y");
			
			// Expressions
			IloLinearNumExpr objective = cplex.linearNumExpr();
			objective.addTerm(0.12, x);
			objective.addTerm(0.15, y);
			
			// Define objective
			cplex.addMinimize(objective);
			
			// Define constraints
			List<IloRange> constraints = new ArrayList<IloRange>();
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(60, x), cplex.prod(60, y)), 300));
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(12, x), cplex.prod(6 , y)),  36));
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(10, x), cplex.prod(30, y)),  90));
			IloLinearNumExpr num_expr = cplex.linearNumExpr();
			num_expr.addTerm(2, x);
			num_expr.addTerm(-1, y);
			constraints.add(cplex.addEq(num_expr, 0));
			num_expr = cplex.linearNumExpr();
			num_expr.addTerm(1, y);
			num_expr.addTerm(-1, x);
			constraints.add(cplex.addLe(num_expr, 8));
			
			cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);
			
			if (cplex.solve()) {
				System.out.println("obj = " + cplex.getObjValue());
				System.out.println("x = " + cplex.getValue(x));
				System.out.println("y = " + cplex.getValue(y));
				
				for (int i = 0 ; i < constraints.size() ; i++) {
					System.out.println("Dual  constraint " + (i+1) + " = " + cplex.getDual(constraints.get(i)));
					System.out.println("Slack constraint " + (i+1) + " = " + cplex.getSlack(constraints.get(i)));
				}
			}
			else {
				System.out.println("Model not solved");
			}
			
			cplex.end();
			
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
}
