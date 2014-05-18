package com.lqyandpy.RBM;

import java.util.ArrayList;

import com.lqyandpy.RBM.Data.Case;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        
	//	RBM tempR=new RBM(7,20);		
		double[][] tempI=new double[][]{
				  {0,0,0,0,0,1,1},
				  {1,0,0,1,0,0,1},
				  {1,0,1,1,0,1,1},
				  {0,1,1,1,0,1,0},
				  {1,1,0,1,0,1,1},
				  {1,1,0,1,1,1,1},
				  {1,0,1,0,0,1,0},
				  {1,1,1,1,1,1,1},
				  {1,1,1,1,0,1,1}
								  };
		
		
		Data tempD=new Data(tempI,false);
		
		/*
		PermanentRBM tempP=new PermanentRBM();
		tempP.ReadFromFile("D:\\rbm8.txt");
		RBM tempR=tempP.ReBuildRBM();*/
		/*
		for(int i=0;i<tempD.getDataCount();i++){
			System.out.println(tempD.countData(i));
		}*/
		/*
		for(int i=0;i<tempD.getDataCount();i++)
			System.out.println(tempR.getFreeEnegy(tempD.getData(i)));*/
	
		/*
		ArrayList<Data> tempSD=tempD.splitMiniBatch(5);
		
		System.out.println(tempSD.size());
		for(Data d:tempSD){
			for(Case c:d.getDataSet()){
				Tool.PrintV(c.getTheCase());
				System.out.println();
			}
			System.out.println();
		}*/
		//System.out.println(tempSD.size());
		
		
		RBM tempR=new RBM(tempD.getDimension(),7,false);
		
		CDTrain tempCT=new CDTrain(tempD,tempR,50);
		tempCT.setLearningRate(0.03);

		//L2 tempL2=new L2();
		//tempL2.setWeightCost(0.001);
		
		tempCT.MiniBatchCD(0.1,null,4);
		
		tempR.SaveAS().WriteToFile("D:\\rbm8.txt");
	
	}

}
