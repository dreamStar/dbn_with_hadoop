package com.lqyandpy.RBM;

public interface WeightDecay {
	public double getWeightDecay(double[][] argW,int argI,int argJ);
	public double getWeightCost();
	public void setWeightCost(double argD);
}
