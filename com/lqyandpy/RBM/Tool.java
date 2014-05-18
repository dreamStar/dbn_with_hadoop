package com.lqyandpy.RBM;

public class Tool {
	public static void PrintW(double[][] argD){
		for(int i=0;i<argD.length;i++){
			for(int j=0;j<argD[0].length;j++){
				System.out.print(argD[i][j]+"  ");
			}
			System.out.println();
		}
	}
	public static void PrintV(double[] argI){
		for(int i=0;i<argI.length;i++){
			System.out.print(argI[i]+"  ");
			
		}
	//	System.out.println();
	}

}
