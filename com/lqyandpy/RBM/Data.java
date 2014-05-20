package com.lqyandpy.RBM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Data {
	private boolean forSupervisedLearning=false;//�����Ƿ�������ǩ
	private ArrayList<Case> dataset=new ArrayList<Case>();
	private int dimension;
	
	public class Case{
		private double[] theCase;
		public double[] getTheCase(){
			return this.theCase;
		}
		public void setTheCase(double[] argD){
			this.theCase=argD;
		}
		
		public int getDimension(){
			return this.theCase.length;
		}
		
		public Case(double[] argD){
			this.setTheCase(argD);
		}
	}
	
	public ArrayList<Case> getDataSet(){
		return this.dataset;
	}
	
	public Data(double[][] argD,boolean argN,boolean labeled){//�Ƿ���Ҫ���滯
		
		this.forSupervisedLearning = labeled;
		if(this.forSupervisedLearning)
			this.dimension=argD[0].length - 1;
		else
			this.dimension=argD[0].length;
		if(argN){
			argD=this.Normalization(argD);
		}
		for(double[] d:argD){
			double[] d_ex;
			if(this.forSupervisedLearning)
			{
				d_ex = new double[d.length-1];
				for(int i = 0;i < d.length-1;++i)
					d_ex[i] = d[i]; 
			}
			else
				d_ex = d; 
			this.dataset.add(new Case(d_ex));
		}
	}
	
	public static double[][] Normalization(double[][] argD){
		double[][] tempD=new double[argD.length][argD[0].length];
		
		for(int j=0;j<tempD[0].length;j++){
			double[] tempC=new double[tempD.length];
			for(int i=0;i<tempC.length;i++){
				tempC[i]=argD[i][j];
			}//�õ���j��
			
			double[] tempDM=Data.Dominator(tempC);
			
			for(int i=0;i<tempC.length;i++){
				tempD[i][j]=tempDM[0]!=0?(tempC[i]-tempDM[1])/tempDM[0]:tempDM[1];
			}
		}
		
		return tempD;
	}

	public static double[] Dominator(double[] argD){
		double max=Double.NEGATIVE_INFINITY;
		double min=Double.POSITIVE_INFINITY;
		
		for(double d:argD){
			if(max<d){
				max=d;
			}
			if(min>d){
				min=d;
			}
		}
		
	//	System.out.println(max+","+min);
		
		return new  double[]{max-min,min};
	}
	//public Data(double[][] argD,)
	
	public Data(int argD){
		this.dimension=argD;
	}
	
	public void addData(Case argC){
		this.dataset.add(argC);
	}
	
	public int getDataCount(){//���ݼ����ж�������
		return this.dataset.size();
	}
	
	public int getDimension(){//���ݵ�ά��
		return this.dimension;
	}
	
	public double[] getData(int argI){//��argI������
		return this.dataset.get(argI).getTheCase();
	}
	
	public double[] getColumn(int argI){//ȡ�õ�argI��
		double[] tempC=new double[this.getDataCount()];
		for(int i=0;i<tempC.length;i++){
			tempC[i]=this.getData(i)[argI];//databag[i][argI];
		}
		return tempC;
	}
	
	
	
	public double getVariableProbability(int argI){//P[data](v(i))
		double[] tempI=this.getColumn(argI);
		int tempS=0;
		for(int i=0;i<tempI.length;i++){
			tempS+=tempI[i];
		}
		
		return (double)tempS/(double)tempI.length;
	}
	
	public ArrayList<Data> splitMiniBatch(int argSZ){
		ArrayList<Data> tempR=new ArrayList<Data>();
		if(!this.forSupervisedLearning){
			LinkedList<Integer> tempIX=new LinkedList<Integer>();
			for(int i=0;i<this.getDataCount();i++){
				tempIX.add(i);
			}
			Collections.shuffle(tempIX);//��������
			
			Data tempD=new Data(this.dimension);
			for(Integer i:tempIX){
				if(tempD.getDataCount()==argSZ){
					tempR.add(tempD);
					tempD=new Data(this.dimension);
				}
				tempD.addData(this.dataset.get(i));
			}
			
			tempR.add(tempD);
			
		}
		
		return tempR;
	}
	
	/*
	public void Normalization(){
		
	}*/
	
	/*
	public double getVariable(int argI,int argJ){//��õ�i�е�j������
		return this.databag[argI][argJ];
	}
	
	public boolean equalData(int argI,int argJ){//�Ƚϵ�argI�����ݺ͵�argJ�������Ƿ���ͬ
		double[] tempI=this.getData(argI);
		double[] tempJ=this.getData(argJ);
		boolean tempB=true;
		for(int i=0;i<tempI.length;i++){
			if(tempI[i]!=tempJ[i]){
				tempB=false;
				break;
			}
		}
		return tempB;
	}
	
	public int countData(int argI){//��argI�����������ݼ��г����˼���
		int tempI=0;
		for(int i=0;i<this.databag.length;i++){
			if(this.equalData(argI, i)){
				tempI++;
			}
		}	
		return tempI;
	}
	
	public double getDataProbability(int argI){//P(V)
		return (double)this.countData(argI)/(double)this.databag.length;
	}
	
	public void splitMiniBatch(int argSZ){
		if(!this.dataForSupervisedLearning){
			LinkedList<Integer> tempIX=new LinkedList<Integer>();
			for(int i=0;i<this.getDataCount();i++){
				tempIX.add(i);
			}
			Collections.shuffle(tempIX);
			
			
			
		}
	}*/
	
}
