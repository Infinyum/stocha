package VLS;

import java.util.ArrayList;
import java.util.Random;

import Model.StochasticProblem;
import Model.Variable;

public class VLSRecuit {
	
	final int NBPalier = 50;
	final int NBIter = 1000;
	
	public StochasticProblem pb;
	
	public ArrayList<Variable> Xinit;
	public double temperature;
	
	public ArrayList<Station> stations;
	
	private ArrayList<Variable> copy(ArrayList<Variable> variables){
		ArrayList<Variable> v = new ArrayList<>();
		
		for(Variable var : variables) {
			v.add(var.copy());
		}
		
		return v;
	}
	
	public int genRandomInt(int max, int min) {
		Random rand = new Random(); 
		return rand.nextInt(max - min + 1) + min;
	}
	
	public void updateVoisinage(ArrayList<Variable> Xmeilleur) {
		//voisinage
		for(int i=0; i<Xmeilleur.size();i++) {
			int rng = genRandomInt(1,-1);
			
			if(Xmeilleur.get(i).val.doubleValue()+rng >= 0 && Xmeilleur.get(i).val.doubleValue()+rng<= stations.get(i).k) {
				Xmeilleur.get(i).val = (double)Xmeilleur.get(i).val+rng;
				//if invalid solution
				if(pb.f.eval()!=1.0f) {
					Xmeilleur.get(i).val = (double)Xmeilleur.get(i).val-rng;
				}
			}
		}
	}
	
	public ArrayList<Variable> solution(double tInit){
		
		ArrayList<Variable> Xmeilleur = copy(Xinit);
		double fmin = pb.f.eval();
		temperature = tInit;
		
		double oldf = pb.f.eval();
		
		for(int i = 0; i<NBPalier;i++) {
			for(int j=0;j<NBIter;j++) {
				double oldfValue = pb.f.eval();
				
				updateVoisinage(Xmeilleur);
				//....
				
			}
		}
		
		
		return Xmeilleur;
		
	}
	
	
}
