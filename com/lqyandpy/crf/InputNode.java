package com.lqyandpy.crf;
import java.util.*;

public class InputNode implements Node {//输入节点，没有bias
	//public ArrayList<Link> Links;
	public double Value;
	public int id;
	public double accumulate;
	//private double delta;
	@Override
	public double getOutput() {
		// TODO Auto-generated method stub
		this.accumulate=this.Value;
	//	System.out.println("我是输入节点 "+this.id+"，输出 "+this.Value);
		return this.Value;
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
		return 3;
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
		return this.accumulate;
	}

	@Override
	public ActivationFunction getActivateFunction() {
		// TODO Auto-generated method stub
		return new IdentityFunction();
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
}
