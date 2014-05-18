package com.lqyandpy.crf;

import java.util.ArrayList;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ANN ann=new ANN();
		
		Integer nn=Integer.MIN_VALUE;
		double[][] tempM=ann.ConstructInitMatrix(new int[][]{{0,nn,nn,nn,nn},// ‰≥ˆ≤„
				                                             {1,2,3,4,nn},
				                                             {5,6,7,8,9},
				                                             {10,11,12,13,nn}// ‰»Î≤„
				                                            });
		
		int[] tempB=new int[]{4,9,13};

		
		ann.InitAnn(tempM, tempB,new TanhFunction(),new TanhFunction());

		double[][] im=new double[][]{{1.2,3.6,1.5,1},
				                     {2.1,1.7,1.7,-1},
				                     {1.7,3.7,3.8,1},
				                     {2.55,6.34,-1.98,-1},
				                     {1.23,7.23,-3.2,1},
				                     {3,4,5,1},
				                     {2,3,6,1},
				                     {1.34,2,2,1},
				                     {2.75,3.66,1.24,-1},
				                     {3.76,2.56,3.67,1},
				                     {2.9,2.57,2.4,-1},
				                     {1.34,3.45,1.75,1}
				                     };
	
		Trainer tempTR=new Trainer();
		tempTR.setLearningRate(0.2);
		
		tempTR.Train(ann, im, 0.01);

	}

}
