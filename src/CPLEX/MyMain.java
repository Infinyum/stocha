package CPLEX;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import org.json.*;

import VLS.ScenarioFactory;
import VLS.Station;

public class MyMain {
	public static void main(String[] args) {
		/*int n = 3;
		double[] c = {0.5, 0.7, 0.6};
		double[] v = {1.5, 1.8, 1.7};
		double[] w = {1.5, 1.9, 1.1};
		int[] k = {10, 8, 12};
		Integer[][] E = {
		          	{ 0, 2, 7},
		         	{13, 0, 5},
		         	{ 4, 3, 0}};
		*/
		
		/*ScenarioFactory scenarioFactory = new ScenarioFactory(1, 3, k);
		for (Map.Entry<Double, Integer[][]> entry : scenarioFactory.getScenarios().entrySet()) {
			VLSwithCPLEX.solveMe(n, c, v, w, k, entry.getValue());			
		}
		System.out.println(scenarioFactory);*/
		
		try
        {
			String content = "";
			content = new String (Files.readAllBytes(Paths.get("dependencies/data.json")));
			String jsonContent = "{\"data\":" + content + "}"; 
			
			JSONObject obj = new JSONObject(jsonContent);
			JSONArray arr = obj.getJSONArray("data");
			
			for (int i = 0; i < 2; i++)
			{
				JSONObject stationJson = new JSONObject(arr.getJSONObject(i).getJSONObject("fields").getString("station"));
				int code = stationJson.getInt("code");
				String name = stationJson.getString("name");
			    int k = arr.getJSONObject(i).getJSONObject("fields").getInt("nbedock");
			    
			    Station myStation = new Station(code, name, k);
			    System.out.println(myStation);
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
