package com.lqyandpy.RBM;

import java.util.ArrayList;
import java.util.Random;

public class GaussRBMNode implements RBMNode {//��˹�ڵ�һ��ֻ���������

	public int id;
	public int type;//0��observ  1��lattent
	public double bias;
	//public ArrayList<PLink> Links;
	public double state=Double.NaN;
	public double variance=1;//�ڵ�ı�׼��
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
	public double getProbability() {//���ڸ�˹�ڵ㣬����һ�㽫������Բ㣬���Ի�ýڵ�ĸ��ʷֲ��Ѿ�û�б�Ҫ��Ĭ�Ϸ���1����
		// TODO Auto-generated method stub
		return 1;
	}
	
	@Override
	public double getState() {
		// TODO Auto-generated method stub
		if(new Double(this.state).equals(Double.NaN)){//����ڵ�״̬�Ѿ�����գ���Ҫ����ȡ��
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
			
			mu=this.bias+this.variance*mu;//�����˹�ֲ��ľ�ֵ
			
			GaussDistribution tempG=new GaussDistribution(mu,this.variance);
			
			this.state=tempG.next();
		}
		return this.state;

	}
	/*
	//�������޸ĺ�ĸ�˹�ڵ��ȡ�ڵ�״̬���롣��ʱ��ȡ�Ľڵ�״̬�Ǹýڵ�ȡ1�ĸ��ʡ�������bug
	public double getProbability(){//�����Ͳ��ùܶԷ��Ǹ�˹�ڵ㻹�Ƕ�ֵ�ڵ���
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
	public double getState() {//�ڵ��״̬
		if(new Double(this.state).equals(Double.NaN)){//״̬û�б���ֵ���Ǵ�ģ�����Ƶ�������
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
