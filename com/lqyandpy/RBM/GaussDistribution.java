package com.lqyandpy.RBM;

public class GaussDistribution {
	public double mu=0;
	public double delta=0.01;
	
	public GaussDistribution(){
		
	}
	
	public GaussDistribution(double argU,double argD){
		this.mu=argU;
		this.delta=argD;
	}
	
	public double next(){
		double tempB=0;
		for(int i=0;i<12;i++){
			tempB+=Math.random();
		}
		
		tempB-=6;
		
		tempB=this.mu+this.delta*tempB;
		return tempB;
	}

}
