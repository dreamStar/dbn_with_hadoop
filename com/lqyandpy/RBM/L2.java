package com.lqyandpy.RBM;

public class L2 implements WeightDecay {

	private double weightcost=0.0001;
	@Override
	public double getWeightDecay(double[][] argW, int argI, int argJ) {
		// TODO Auto-generated method stub
		return this.getWeightCost()*argW[argI][argJ];
	}

	@Override
	public double getWeightCost() {
		// TODO Auto-generated method stub
		return this.weightcost;
	}

	@Override
	public void setWeightCost(double argD) {
		// TODO Auto-generated method stub
		this.weightcost=argD;

	}

}
