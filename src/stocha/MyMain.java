package stocha;

public class MyMain {
	public static void main(String[] args) {
		int n = 3;
		double[] c = {0.5, 0.7, 0.6};
		double[] v = {1.5, 1.8, 1.7};
		double[] w = {1.5, 1.9, 1.1};
		int[] k = {10, 8, 12};
		int[][] E = {
		          	{ 0, 2, 7},
		         	{13, 0, 5},
		         	{ 4, 3, 0}};
		
		
		VLSwithCPLEX.solveMe(n, c, v, w, k, E);
	}
}
