package VLS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import Model.Recuit;

public class VLSSolver {
	public static void main(String[] args) throws IOException {
		// RECUIT SETTINGS
		double temperatureInit = 40d;
		double coef = 0.9d;
		double epsilon = 1E-3;
		//int nbPaliers = stations.size() * 2;
		int nbPaliers = 1000;
		int nbRounds = 1; // Number of times penalty applied
		int nbScenarios = 1;
		
		
		// CUSTOM EXAMPLE
		Integer[][] E = {
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
 		Xinit.add(5); Xinit.add(4); Xinit.add(6);
 		VLSNeighbourhood neighbourhood = new VLSNeighbourhood(myStations);
 		VLSConstraints constraints = new VLSConstraints(myStations, E);
		VLSTargetFunction objective = new VLSTargetFunction(myStations, E);
		// END CUSTOM EXAMPLE
		
		
		// PARSING JSON DATA
		String content = "";
		content = new String (Files.readAllBytes(Paths.get("dependencies/data.json")));
		String jsonContent = "{\"data\":" + content + "}"; 
		JSONObject obj = new JSONObject(jsonContent);
		JSONArray arr = obj.getJSONArray("data");
		
		int code, k;
		String name;
		List<Station> stations = new ArrayList<>();
		//List<Integer> Xinit = new ArrayList<>();
		
		// CREATING STATION OBJECTS
		for (int i = 0; i < 50/*arr.length()*/; i++)
		{
			k = arr.getJSONObject(i).getJSONObject("fields").getInt("nbedock");
			// Initial solution: stations are filled halfway
			//Xinit.add((int)Math.round(k/2.0));
			
			JSONObject stationJson = new JSONObject(arr.getJSONObject(i).getJSONObject("fields").getString("station"));
			code = stationJson.getInt("code");
			name = stationJson.getString("name");
		    
		    stations.add(new Station(code, name, k));
		}
		
		//VLSNeighbourhood neighbourhood = new VLSNeighbourhood(stations);
		
		
		//VLSTargetFunction objective = new VLSTargetFunction(stations, entry.getValue());
		//VLSConstraints constraints = new VLSConstraints(stations, entry.getValue());
		ScenarioFactory scenarioFactory = new ScenarioFactory(nbScenarios, stations);
		Recuit myRecuit = new Recuit(temperatureInit, coef, epsilon, nbPaliers, objective, neighbourhood, constraints);
		//List<List<Integer>> subSolutions = new ArrayList<>();
		
		int round = 0;
		
		while (round < nbRounds) {
			// SOLVE FOR EACH SCENARIO
			for (Map.Entry<Double, Integer[][]> entry : scenarioFactory.getScenarios().entrySet()) {
				// UPDATE OBJECTIVE AND CONSTRAINTS FOR THIS SCENARIO
				/*objective.setDemandMatrix(entry.getValue());
				myRecuit.setObjective(objective);
				constraints.setDemandMatrix(entry.getValue());
				myRecuit.setConstraints(constraints);*/

				List<Integer> subSol = myRecuit.solveMe(Xinit);
				System.out.println("X = " + subSol);
			}
			
			// TODO: penalite pour chaque sous-recuit puis repartir pour une nouvelle iteration
			// ...
			
			round++;
		}
	}
}
