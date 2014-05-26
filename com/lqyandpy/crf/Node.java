package com.lqyandpy.crf;
import java.io.Serializable;
import java.util.*;

public interface Node extends Comparable<Node> , Serializable{
	public double getOutput();//��Inputת��Output
	public int getFanIn();//�ڵ������
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
