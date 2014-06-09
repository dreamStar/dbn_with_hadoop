package com.lqyandpy.crf;

import java.util.ArrayList;

public class BiasNode implements Node {
	public int id;
	
	private double accumulate=1;
	//public double bias;
	@Override
	public double getOutput() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getFanIn() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public int getNodeType() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int compareTo(Node y) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.id).compareTo(Integer.valueOf(y.getID()));
	}

	@Override
	public void setActivateFunction(ActivationFunction argF) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Link> getLinks() {
		// TODO Auto-generated method stub
		return new ArrayList<Link>();
	}

	@Override
	public double getCachedAccumulate() {
		// TODO Auto-generated method stub
		return accumulate;
	}

	@Override
	public ActivationFunction getActivateFunction() {
		// TODO Auto-generated method stub
		return new ConstantFunction();
	}

	@Override
	public double getdelta() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setdelta(double argD) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearNodeRecursive() {
		// TODO Auto-generated method stub
		
	}

}
