package com.lqyandpy.RBM;

import java.util.*;

import com.lqyandpy.crf.Node;

public class RBM {
	public double[][] W;//权值向量
	public ArrayList<RBMNode> vNodes=new ArrayList<RBMNode>();//节点
	public ArrayList<RBMNode> hNodes=new ArrayList<RBMNode>();
	public int hn;//隐节点数目，矩阵的列数
	public int vn;//显节点数目，矩阵的行数
	public boolean type;//true:gaussRBM,false:binaryRBM
	
	public RBM(int argV,int argH,boolean argG){//argG true GaussRBM
		//给定隐藏层和可见层节点数目
		double[][] tempD=new double[argV][argH];
		this.hn=argH;
		this.vn=argV;
		
		GaussDistribution tempG=new GaussDistribution();
		
		for(int i=0;i<argV;i++)
			for(int j=0;j<argH;j++)
				tempD[i][j]=tempG.next();
		this.W=tempD;
		
		this.type=argG;
		
		this.ConstructRBM(this.W,argG);
	}

	public RBM(double[][] argD,boolean argG){
		//给定权重来构建RBM
		this.vn=argD.length;
		this.hn=argD[0].length;
		this.W=argD;
		this.ConstructRBM(this.W,argG);
		
	}
	
//	public RBMNode getNode(int argID,int argT){//类型和id
//		RBMNode tempN=null;
//		for(RBMNode n:Nodes){
//			if(n.getID()==argID&&n.getType()==argT){
//				tempN=n;
//				break;
//			}
//		}
//		return tempN;
//	}
	
	public RBMNode getNode(int argID,int argT){//类型和id
		RBMNode tempN= argT == 0?this.vNodes.get(argID):this.hNodes.get(argID);
		
		return tempN;
	}
	
	public double[][] WT(){//W的转置矩阵
		double[][] tempRes=new double[this.W[0].length][this.W.length];
		
		for(int i=0;i<tempRes.length;i++){
			for(int j=0;j<tempRes[0].length;j++){
				tempRes[i][j]=this.W[j][i];
			}
		}
		
	    return tempRes;
	}
	

	
	public double[] getWRow(int argD){
		return this.W[argD];
	}
	
	public double[] getWColumn(int argD){
		double[] tempRes=new double[this.W.length];
		for(int i=0;i<tempRes.length;i++){
			tempRes[i]=this.W[i][argD];
		}
		
		return tempRes;
	}
	
	
	public void setNodeState(double[] argI,int argT){
		for(int i=0;i<argI.length;i++){
			this.getNode(i,argT).setState(argI[i]);
		}
	}
	
	public double[] getNodeState(int argT){//argT 1隐/0显
		double[] tempI=new double[argT==0?this.vn:this.hn];
		
		for(int i=0;i<tempI.length;i++){
			tempI[i]=this.getNode(i, argT).getState();
		}
		
		return tempI;
	}
	
//	public void clearNodeState(int argT){
//		for(RBMNode n:this.Nodes){
//			if(n.getType()==argT){
//				n.clearState();
//			}
//		}
//	}
//	
//	public void UpdateRBM(){
//		for(RBMNode n:this.Nodes){
//			double[] tempW=n.getType()==0?this.getWRow(n.getID()):this.getWColumn(n.getID());
//			for(PLink l:n.getLinks()){
//				l.weight=tempW[l.end.getID()];
//			}	
//		}
//	}
	
	public void clearNodeState(int argT){
		ArrayList<RBMNode> tmp = argT==0?this.vNodes:this.hNodes;
		for(RBMNode n:tmp){
				n.clearState();
		}
	}
	
//	public void UpdateRBM(){
//		for(RBMNode n:this.vNodes){
//			double[] tempW=this.getWRow(n.getID());
//			for(PLink l:n.getLinks()){
//				l.weight=tempW[l.end.getID()];
//			}	
//		}
//		for(RBMNode n:this.hNodes){
//			double[] tempW=this.getWColumn(n.getID());
//			for(PLink l:n.getLinks()){
//				l.weight=tempW[l.end.getID()];
//			}	
//		}
//	}
	
	public PermanentRBM SaveAS(){
		PermanentRBM tempP=new PermanentRBM();
		tempP.weight=this.W;
		double[] biasv=new double[this.vn];
		double[] biash=new double[this.hn];
		
//		for(RBMNode n:this.Nodes){
//			if(n.getType()==0){
//				biasv[n.getID()]=n.getBias();
//			}else{
//				biash[n.getID()]=n.getBias();
//			}
//		}
		
		for(RBMNode n:this.vNodes)
			biasv[n.getID()] = n.getBias();
		for(RBMNode n:this.hNodes)
			biash[n.getID()] = n.getBias();
		
		tempP.biasv=biasv;
		tempP.biash=biash;
		
		tempP.type=this.type;
		
		return tempP;
	}
	
	public RBM CopyTiedRBM(){//这个方法专用于贪心逐层训练RBM，因此都是二值RBM
		double[][] tempW=this.WT();
		RBM tempRBM=new RBM(tempW,false);
		
//		for(RBMNode n:tempRBM.Nodes){
//			n.setBias(this.getNode(n.getID(), (n.getType()+1)%2).getBias());//初始化上层网络的bias		
//		}
		for(RBMNode n:tempRBM.vNodes)
			n.setBias(this.getNode(n.getID(), (n.getType()+1)%2).getBias());
		for(RBMNode n:tempRBM.hNodes)
			n.setBias(this.getNode(n.getID(), (n.getType()+1)%2).getBias());
		return tempRBM;
	}
	
	public RBM CopyTiedRBM(int hnode,int vnode){//这个方法专用于贪心逐层训练RBM，因此都是二值RBM
		
		RBM tempRBM=new RBM(vnode,hnode,false);
		
		return tempRBM;
	}
	
	public double getFreeEnegy(double[] argI){//获得显层节点的自由能
		double tempFE=1;
		for(int i=0;i<this.hn;i++){
			double tempHO=0;
			for(int j=0;j<this.vn;j++){
				tempHO+=argI[j]*this.W[j][i];
			}
			tempHO=1+Math.exp(tempHO);
			tempFE*=tempHO;
		}
		return tempFE;
	}
	
	public void ConstructRBM(double[][] argD,boolean argG){
		//从矩阵构造图,列数对应visible的数目，行数对应hidden的数目，argG指示是否创建guassrbm
		int tempV=this.vn;//显节点数目
		int tempH=this.hn;//隐节点数目
		
		for(int i=0;i<tempH;i++){//初始化隐节点，bias用随机数
			BasicRBMNode tempR=new BasicRBMNode(this);
			tempR.setType(1);
			tempR.setBias(Math.random());
			tempR.setID(i);
			//this.Nodes.add(tempR);
			this.hNodes.add(tempR);
		}
		
		for(int i=0;i<tempV;i++){//初始化显节点，bias用随机数
			RBMNode tempR;
			if(!argG){
				tempR=new BasicRBMNode(this);
			}else{
				tempR=new GaussRBMNode(this);
			}
			tempR.setType(0);
			tempR.setBias(Math.random());//BUG
			tempR.setID(i);
			//this.Nodes.add(tempR);
			this.vNodes.add(tempR);
		}
		
		
//		for(RBMNode n:this.Nodes){
//			ArrayList<PLink> tempLKS=new ArrayList<PLink>(); 
//			double[] tempW=n.getType()==0?this.getWRow(n.getID()):this.getWColumn(n.getID());//权向量
//			for(int i=0;i<tempW.length;i++){
//				PLink tempL=new PLink();
//				tempL.weight=tempW[i];
//				tempL.end=this.getNode(i, (n.getType()+1)%2);
//				tempLKS.add(tempL);
//			}
//			n.setLinks(tempLKS);
//		
//		}
		
//		for(RBMNode n:this.vNodes){
//			ArrayList<PLink> tempLKS=new ArrayList<PLink>(); 
//			double[] tempW=this.getWRow(n.getID());//权向量
//			for(int i=0;i<tempW.length;i++){
//				PLink tempL=new PLink();
//				tempL.weight=tempW[i];
//				tempL.end=this.getNode(i, 1);
//				tempLKS.add(tempL);
//			}
//			n.setLinks(tempLKS);
//		
//		}
//		
//		for(RBMNode n:this.hNodes){
//			ArrayList<PLink> tempLKS=new ArrayList<PLink>(); 
//			double[] tempW=this.getWColumn(n.getID());//权向量
//			for(int i=0;i<tempW.length;i++){
//				PLink tempL=new PLink();
//				tempL.weight=tempW[i];
//				tempL.end=this.getNode(i, 0);
//				tempLKS.add(tempL);
//			}
//			n.setLinks(tempLKS);
//		
//		}
		
		int a = 1;
		
	}

}
