package com.lqyandpy.crf;
import java.io.Serializable;
import java.util.*;

public interface Node extends Comparable<Node> , Serializable{
	public double getOutput();//锟斤拷Input转锟斤拷Output
	public int getFanIn();//锟节碉拷锟斤拷锟斤拷锟�
	public int getNodeType();
	public int getID();
	public void setActivateFunction(ActivationFunction argF);
	public ActivationFunction getActivateFunction();
	public ArrayList<Link> getLinks();
	int compareTo(Node y);
	public double getCachedAccumulate();
	public double getdelta();
	public void setdelta(double argD);
	public void clearNodeRecursive();
	//double getCachedOutput();
}
