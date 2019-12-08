package VLS;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import Model.Recuit;

public class VLSSolver {
	public static Double solve(int S, ArrayList<Station> stations) {
		// RECUIT SETTINGS
		//double temperatureInit = 6.4E40d;
		double temperatureInit = 40d;
		double coef = 0.9d;
		double alpha = 1.1d;
		double epsilon = 1E-3;
		int nbIter = 1000;
		int nbRounds = 10; // Number of times penalty applied
		int n = stations.size();
		//int S = 5;
		
		// Initialize Xinit
		List<Integer> Xinit = new ArrayList<>();
		for (int i = 0 ; i < n ; i++) {
			Xinit.add((int)Math.round(stations.get(i).getK()/2.0));
		}
		
		
		// CUSTOM EXAMPLE
		/*Integer[][] E = {
		      	{ 0, 2, 7},
		     	{13, 0, 5},
		     	{ 4, 3, 0}};

		Station s1 = new Station(1, "One", 10);
		s1.setC(0.5); s1.setV(1.5); s1.setW(1.5);
		Station s2 = new Station(2, "Two", 8);
		s2.setC(0.7); s2.setV(1.8); s2.setW(1.9);
		Station s3 = new Station(3, "Three", 12);
		s3.setC(0.6); s3.setV(1.7); s3.setW(1.1);
		List<Station> myStations = new ArrayList<>();
		myStations.add(s1); myStations.add(s2); myStations.add(s3);
		
		List<Integer> Xinit = new ArrayList<>();
 		Xinit.add(5); Xinit.add(4); Xinit.add(6);*/
		// END CUSTOM EXAMPLE
		
		
		// PARSING JSON DATA
		/*String content = "";
		content = new String (Files.readAllBytes(Paths.get("dependencies/data.json")));
		String jsonContent = "{\"data\":" + content + "}"; 
		JSONObject obj = new JSONObject(jsonContent);
		JSONArray arr = obj.getJSONArray("data");
		
		int code, k;
		String name;
		List<Station> stations = new ArrayList<>();*/
		
		
		// CREATING STATION OBJECTS
		/*for (int i = 0; i < arr.length(); i++)
		{
			k = arr.getJSONObject(i).getJSONObject("fields").getInt("nbedock");
			// Initial solution: stations are filled halfway
			Xinit.add((int)Math.round(k/2.0));
			
			JSONObject stationJson = new JSONObject(arr.getJSONObject(i).getJSONObject("fields").getString("station"));
			code = stationJson.getInt("code");
			name = stationJson.getString("name");
		    
		    stations.add(new Station(code, name, k));
		}*/
		
		

		// Penalty variables
		List<Double> phi = new ArrayList<>();
		List<Double> lambda = new ArrayList<>();
		List<Integer> Xbarre = new ArrayList<>();
		for (int i = 0 ; i < n ; i++) {
			phi.add(stations.get(i).getC() / 2d);
			lambda.add(0d);
		}
		
		VLSNeighbourhood neighbourhood = new VLSNeighbourhood(stations);
		List<Scenario> scenarios = new ScenarioFactory(S, stations).getScenarios();
		
		// Initialize sous-recuits
		for (Scenario s : scenarios) {
			VLSConstraints constraints = new VLSConstraints(stations, s.getDemandMatrix());
			VLSTargetFunction objective = new VLSTargetFunction(stations, s.getDemandMatrix());
			
			s.setRecuit(new Recuit(temperatureInit, coef, epsilon, nbIter, objective, neighbourhood, constraints, Xinit));
			s.getRecuit().getObjective().setLambda(new ArrayList<Double>(lambda));
			s.getRecuit().getObjective().setPhi(phi);
		}
		
		
		int round = 0;
		
		while (round < nbRounds) {
			Xbarre.clear();
			// Compute Xbarre
			double temp = 0d;
			for (int i = 0 ; i < n ; i++) {
				Xbarre.add(0);
				temp = 0d;
				for (Scenario s : scenarios) {
					temp += s.getProbability() * s.getRecuit().getSolution().get(i);
				}
				Xbarre.set(i, (int)Math.round(temp));
			}
			
			// SOLVE FOR EACH SCENARIO			
			for (Scenario s : scenarios) {
				s.getRecuit().getObjective().setXbarre(Xbarre);
				
				// No need to update for the first round (already initialized)
				if (round > 0) {
					// Update phi and lambda in the target function
					s.getRecuit().getObjective().update(phi, s.getRecuit().getSolution());
				}
				
				s.getRecuit().solveMe();
			}

			// Update phi
			for (int i = 0 ; i < n ; i++) {
				phi.set(i, alpha * phi.get(i));
			}		
			
			round++;
		}
		
		// Compute Xfinal and objFinal
		List<Integer> Xfinal = new ArrayList<>();
		double temp = 0d;
		for (int i = 0 ; i < n ; i++) {
			Xfinal.add(0);
			temp = 0d;
			for (Scenario s : scenarios) {
				temp += s.getProbability() * s.getRecuit().getSolution().get(i);
			}
			Xfinal.set(i, (int)Math.round(temp));
			stations.get(i).setAction(Xfinal.get(i));
		}
		
		Double objFinal = new Double(0d);
		for (Scenario s : scenarios) {			
			objFinal += s.getProbability() * s.getRecuit().getObjectiveValue();
			for (int i = 0 ; i < n ; i++) {
				objFinal += s.getRecuit().getObjective().getLambda().get(i) * Xfinal.get(i);
			}
		}
		for (int i = 0 ; i < n ; i++) {
			objFinal += 0.5d * phi.get(i) * Xfinal.get(i) * Xfinal.get(i);
		}
		
		System.out.println("Solution = " + BigDecimal.valueOf(objFinal).setScale(1, RoundingMode.HALF_UP).doubleValue());
		System.out.println(Xfinal);
		
		return objFinal;
	}
}
