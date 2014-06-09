package com.lqyandpy.crf;

import java.util.ArrayList;

public class OutputNode implements Node {
    public ArrayList<Link> Links=new ArrayList<Link>();
    public int id;
    private ActivationFunction func;
    private Double accumulate = Double.NaN;
    private double delta;
	@Override
	public double getOutput() {
		double tempSum=0;
		for(Link l:Links){
			Node tempN=l.From;
			tempSum+=l.Weight*tempN.getOutput();
		}
		
		this.accumulate=tempSum;
		
		return func.evaluate(this.accumulate);
	}
	public void clearNodeRecursive()
	{
		
		this.accumulate = Double.NaN;
		for (Link l : Links) {
			Node tempN = l.From;
			tempN.clearNodeRecursive();
		}
	}
	@Override
	public int getFanIn() {
		// TODO Auto-generated method stub
		return Links.size();
	}


	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public int getNodeType() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int compareTo(Node y) {
		// TODO Auto-generated method stub
		return Integer.valueOf(this.id).compareTo(Integer.valueOf(y.getID()));
	}

	@Override
	public void setActivateFunction(ActivationFunction argF) {
		// TODO Auto-generated method stub
		this.func=argF;
	}

	@Override
	public ArrayList<Link> getLinks() {
		// TODO Auto-generated method stub
		return this.Links;
	}

	@Override
	public double getCachedAccumulate() {
		// TODO Auto-generated method stub
		return this.accumulate;
	}

	@Override
	public ActivationFunction getActivateFunction() {
		// TODO Auto-generated method stub
		return this.func;
	}

	
	@Override
	public double getdelta() {
		// TODO Auto-generated method stub
		return this.delta;
	}

	@Override
	public void setdelta(double argD) {
		// TODO Auto-generated method stub
		this.delta=argD;
	}
}
