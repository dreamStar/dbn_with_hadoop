package com.lqyandpy.RBM;

import java.util.ArrayList;

public interface RBMNode {

	public abstract int getID();
	public abstract void setID(int argI);
	
	public abstract void setState(double argS);

	public abstract void clearState();

	public abstract int getType();
	public abstract void setType(int argT);

	public abstract double getBias();
	public abstract void setBias(double argD);

	public abstract double getProbability();

	public abstract double getState();

	public abstract PLink findLink(int argE);
	public abstract ArrayList<PLink> getLinks();
	public abstract void setLinks(ArrayList<PLink> argL);
	
	public abstract double getVariance();

}