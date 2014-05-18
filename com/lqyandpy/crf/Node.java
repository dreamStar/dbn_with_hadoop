package com.lqyandpy.crf;
import java.util.*;

public interface Node extends Comparable<Node>{
	public double getOutput();//将Input转成Output
	public int getFanIn();//节点的扇入
	public int getNodeType();
	public int getID();
	public void setActivateFunction(ActivationFunction argF);
	public ActivationFunction getActivateFunction();
	public ArrayList<Link> getLinks();
	int compareTo(Node y);
	public double getCachedAccumulate();
	public double getdelta();
	public void setdelta(double argD);
	
	//double getCachedOutput();
}
