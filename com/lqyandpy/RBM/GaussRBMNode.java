package com.lqyandpy.RBM;

import java.util.ArrayList;
import java.util.Random;

public class GaussRBMNode implements RBMNode {//高斯节点一般只用于输入层

	public int id;
	public int type;//0：observ  1：lattent
	public double bias;
	//public ArrayList<PLink> Links;
	public double state=Double.NaN;
	public double variance=1;//节点的标准差
	public RBM rbm;
	
	public GaussRBMNode(RBM parent)
	{
		this.rbm = parent;
	}
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public void setState(double argS) {
		// TODO Auto-generated method stub
		this.state=argS;

	}

	@Override
	public void clearState() {
		// TODO Auto-generated method stub
		this.state=Double.NaN;

	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	@Override
	public double getBias() {
		// TODO Auto-generated method stub
		return this.bias;
	}
	
	@Override
	public double getProbability() {//对于高斯节点，由于一般将其放在显层，所以获得节点的概率分布已经没有必要，默认返回1即可
		// TODO Auto-generated method stub
		return 1;
	}
	
	@Override
	public double getState() {
		// TODO Auto-generated method stub
		if(new Double(this.state).equals(Double.NaN)){//如果节点状态已经被清空，需要重新取样
			double mu=0;
//			for(PLink l:this.Links){
//				mu+=l.weight*l.end.getState();
//			}
			
			RBM r = this.rbm;
			if(this.type == 0)
			{
				for(int i = 0;i < r.hNodes.size();++i)
					mu += r.W[this.id][i]*r.hNodes.get(i).getState();
			}
			else
			{
				for(int i = 0;i < r.vNodes.size();++i)
					mu += r.W[i][this.id]*r.vNodes.get(i).getState();
			}
			
			mu=this.bias+this.variance*mu;//计算高斯分布的均值
			
			GaussDistribution tempG=new GaussDistribution(mu,this.variance);
			
			this.state=tempG.next();
		}
		return this.state;

	}
	/*
	//以下是修改后的高斯节点获取节点状态代码。此时获取的节点状态是该节点取1的概率。现在有bug
	public double getProbability(){//这样就不用管对方是高斯节点还是二值节点了
    	double tempT=0;
		
		for(PLink l:this.Links){
			tempT+=l.weight*l.end.getState()/l.end.getVariance();
		}
	
		tempT+=this.bias;
		
		if(tempT<-45){
			return 0;
		}else if(tempT>45){
			return 1;
		}else{
			double tempP=1/(1+Math.pow(Math.E, (-1d)*tempT));
			return tempP;
		}
    }
	
	@Override
	public double getState() {//节点的状态
		if(new Double(this.state).equals(Double.NaN)){//状态没有被赋值，是从模型中推导出来。
			this.state = this.getProbability();
		}
	
		return this.state;
	}
	*/

//	@Override
//	public PLink findLink(int argE) {
//		// TODO Auto-generated method stub
//		PLink tempL=null;
//		for(PLink l:this.Links){
//			if(l.end.getID()==argE){
//				tempL=l;
//				break;
//			}
//		}
//		return tempL;
//	}

	@Override
	public double getVariance() {
		// TODO Auto-generated method stub
		return this.variance;
	}

	@Override
	public void setID(int argI) {
		// TODO Auto-generated method stub
		this.id=argI;
	}

	@Override
	public void setType(int argT) {
		// TODO Auto-generated method stub
		this.type=argT;
	}

	@Override
	public void setBias(double argD) {
		// TODO Auto-generated method stub
		this.bias=argD;
	}

//	@Override
//	public ArrayList<PLink> getLinks() {
//		// TODO Auto-generated method stub
//		return this.Links;
//		
//	}

//	@Override
//	public void setLinks(ArrayList<PLink> argL) {
//		// TODO Auto-generated method stub
//		this.Links=argL;
//	}
	
	

}
