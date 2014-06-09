package com.lqyandpy.crf;

import java.util.ArrayList;

public class HiddenNode implements Node {
	
	public ArrayList<Link> Links=new ArrayList<Link>();
	public int id;
	private ActivationFunction func;
	private Double cache = Double.NaN;
	private double delta;
//	public double bias;
	
	@Override
	public double getOutput() {
		if(this.cache.isNaN())
		{
			double tempSum = 0;
			for (Link l : Links) {
				Node tempN = l.From;
				tempSum += l.Weight * tempN.getOutput();
			}
			// tempSum+=bias;
			this.cache = tempSum;
		}
		
	//	System.out.println("我是隐层节点 "+this.id+"，输出 "+func.evaluate(tempSum));
		
		return func.evaluate(this.cache);
	}
	
	public void clearNodeRecursive()
	{
		if(this.cache.isNaN())
			return;
		this.cache = Double.NaN;
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
		return 2;
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
		return cache;
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
	//	System.out.println("我是隐层节点 "+this.id+"，delta "+argD);
		this.delta=argD;
	}

}
