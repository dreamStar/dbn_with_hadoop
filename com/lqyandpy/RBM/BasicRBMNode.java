package com.lqyandpy.RBM;

import java.util.ArrayList;

public class BasicRBMNode implements RBMNode{//

	public int id;
	public int type;//0：observ  1：lattent
	public double bias;
	public ArrayList<PLink> Links;
	public double state=Double.NaN;
	public double variance=1;//为了方便计算，定义二值节点的标准差为1

	/* (non-Javadoc)
	 * @see com.lqyandpy.RBM.RBMNode#getID()
	 */
	@Override
	public int getID() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see com.lqyandpy.RBM.RBMNode#setState(double)
	 */
	@Override
	public void setState(double argS){
	
			this.state=argS;
	
	}
	
	/* (non-Javadoc)
	 * @see com.lqyandpy.RBM.RBMNode#clearState()
	 */
	@Override
	public void clearState(){
		this.state=Double.NaN;
	}
	
	/* (non-Javadoc)
	 * @see com.lqyandpy.RBM.RBMNode#getType()
	 */
	@Override
	public int getType() {
		return this.type;
	}


	/* (non-Javadoc)
	 * @see com.lqyandpy.RBM.RBMNode#getBias()
	 */
	@Override
	public double getBias() {
		return this.bias;
	}

    /* (non-Javadoc)
	 * @see com.lqyandpy.RBM.RBMNode#getProbability()
	 */
    @Override
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

	
	/* (non-Javadoc)
	 * @see com.lqyandpy.RBM.RBMNode#getState()
	 */
	@Override
	public double getState() {//节点的状态
		if(new Double(this.state).equals(Double.NaN)){//状态没有被赋值，是从模型中推导出来。
			if(this.getProbability()>Math.random()){
				this.state=1;
			}else{
				this.state=0;
			}
		}
	
		return this.state;
	}
	
	/* (non-Javadoc)
	 * @see com.lqyandpy.RBM.RBMNode#findLink(int)
	 */
	@Override
	public PLink findLink(int argE){
		PLink tempL=null;
		for(PLink l:this.Links){
			if(l.end.getID()==argE){
				tempL=l;
				break;
			}
		}
		return tempL;
	}
	
	public double getVariance(){
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

	@Override
	public ArrayList<PLink> getLinks() {
		// TODO Auto-generated method stub
		return this.Links;
	}

	@Override
	public void setLinks(ArrayList<PLink> argL) {
		// TODO Auto-generated method stub
		this.Links=argL;
	}
	
}
