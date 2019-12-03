package CPLEX;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.json.*;

import VLS.ScenarioFactory;
import VLS.Station;

public class MyMain {
	public static void main(String[] args) {
		/*double[] c = {0.5, 0.7, 0.6};
		double[] v = {1.5, 1.8, 1.7};
		double[] w = {1.5, 1.9, 1.1};
		int[] k = {10, 8, 12};*/
		
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
		myStations.add(s1); myStations.add(s2); myStations.add(s3);*/
		
		try
        {
			// Parsing the JSON data
			String content = "";
			content = new String (Files.readAllBytes(Paths.get("dependencies/data.json")));
			String jsonContent = "{\"data\":" + content + "}"; 
			JSONObject obj = new JSONObject(jsonContent);
			JSONArray arr = obj.getJSONArray("data");
			
			int code, k;
			String name;
			List<Station> stations = new ArrayList<>();
			
			// Creating the Station objects from the JSON data
			for (int i = 0; i < 9/*arr.length()*/; i++)
			{
				k = arr.getJSONObject(i).getJSONObject("fields").getInt("nbedock");

				JSONObject stationJson = new JSONObject(arr.getJSONObject(i).getJSONObject("fields").getString("station"));
				code = stationJson.getInt("code");
				name = stationJson.getString("name");
			    
			    stations.add(new Station(code, name, k));
			}
			
			ScenarioFactory scenarioFactory = new ScenarioFactory(1, stations);
			// Solve for each scenario
			for (Map.Entry<Double, Integer[][]> entry : scenarioFactory.getScenarios().entrySet()) {
				VLSwithCPLEX.solveMe(stations, entry.getValue());
			}
			
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
