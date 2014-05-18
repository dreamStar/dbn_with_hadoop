package com.lqyandpy.RBM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.lqyandpy.RBM.Data.Case;

public class CDTrain {
	
	private Data dataSet;
	private RBM rbm;
	private double rate=0.001;
	private Random r=new Random();
	private int max_try = 50;
	public CDTrain(Data argD,RBM argR,int max_try){
		this.dataSet=argD;
		this.rbm=argR;
		this.max_try = max_try;
		for(RBMNode n:this.rbm.vNodes){//从训练数据初始化bias
			//if(n.getType()==0){
//				double tempP=this.dataSet.getVariableProbability(n.getID());
//				if(tempP==0){
//					n.setBias(-4);
//				}else if(tempP==1){
//					n.setBias(4);
//				}else{n.setBias(Math.log(tempP/(1-tempP)));}
				n.setBias(r.nextDouble());
			//}
		}
	}
	
	public void setLearningRate(double argD){
		this.rate=argD;
	}

	public RBM getRBM(){
		return this.rbm;
	}
	
	public double Errorta(){
		System.out.println("--------------------------------计算误差----------------------------------");
		double tempE=0;
		
		for(Case c:this.dataSet.getDataSet()){
			this.rbm.clearNodeState(0);
			this.rbm.clearNodeState(1);
			
			double[] v=c.getTheCase();
			this.rbm.setNodeState(v, 0);//为可见节点赋值clamp
			double[] h0=this.rbm.getNodeState(1);//对隐含层节点取样
			this.rbm.clearNodeState(0);//清空可见层
			double[] v1=this.rbm.getNodeState(0);//对可见层取样
			this.rbm.clearNodeState(1);//清空隐含层。
			
			double tempForm=0;
			for(int i=0;i<v.length;i++){
				tempForm+=Math.pow((double)v[i]-v1[i], 2);
			}

			tempE+=Math.sqrt(tempForm);
		}
		
		return tempE;
	}
	
	public void CD(double argSC,WeightDecay argWD){//基本CD，RE停机条件，L2或L1
		int epoch=0;
		while(true){
			for(int i=0;i<this.dataSet.getDataCount();i++){//开始一轮训练
				this.rbm.clearNodeState(0);
				this.rbm.clearNodeState(1);//打扫干净两层的残留状态

				double[] v0=this.dataSet.getData(i);
				this.rbm.setNodeState(v0, 0);//设置可见层的状态为V0
				double[] tempHCV0=new double[this.rbm.hn];//计算隐层的概率
//				for(RBMNode n:this.rbm.Nodes){
//					if(n.getType()==1){
//						tempHCV0[n.getID()]=n.getProbability();
//					}
//				}
				for(RBMNode n:this.rbm.hNodes)
						tempHCV0[n.getID()]=n.getProbability();
				double[] h0=this.rbm.getNodeState(1);//取样H0;
				this.rbm.clearNodeState(0);//清空可见层
				double[] v1=this.rbm.getNodeState(0);//取样V1
				this.rbm.clearNodeState(1);//清空隐藏层
				double[] tempHCV1=new double[this.rbm.hn];//计算隐藏层节点的概率
				for(RBMNode n:this.rbm.hNodes)
						tempHCV1[n.getID()]=n.getProbability();
				
				for(int v=0;v<this.rbm.vn;v++){
					for(int h=0;h<this.rbm.hn;h++){
						double tempDW=((double)v0[v]*tempHCV0[h]-(double)v1[v]*tempHCV1[h])/this.rbm.getNode(v, 0).getVariance();
						if(argWD!=null){
							tempDW+=argWD.getWeightDecay(this.rbm.W, v, h);
						}
						this.rbm.W[v][h]+=this.rate*tempDW;
					}
				}

				this.rbm.UpdateRBM();//更新RBM,矩阵只表示了互连权值，节点的偏移值要另外设置
//				for(RBMNode n:this.rbm.Nodes){//更新节点bias
//					if(n.getType()==0){//更新可见层的bias
//						n.setBias(n.getBias()+this.rate*((v0[n.getID()]-v1[n.getID()])/n.getVariance()));
//					}else{//更新隐藏层的bias
//						n.setBias(n.getBias()+this.rate*(tempHCV0[n.getID()]-tempHCV1[n.getID()]));
//					}
//				}
				for(RBMNode n:this.rbm.vNodes)
					n.setBias(n.getBias()+this.rate*((v0[n.getID()]-v1[n.getID()])/n.getVariance()));
				for(RBMNode n:this.rbm.hNodes)
					n.setBias(n.getBias()+this.rate*(tempHCV0[n.getID()]-tempHCV1[n.getID()]));
			}
			
			epoch++;
			double tempE=this.Errorta();
			System.out.println("第"+epoch+" 次训练，重构误差"+tempE);
			if(tempE<=argSC){
				System.out.println("经过"+epoch+" 次训练，RBM 收敛");
				break;				
			}
			
		}
		
		
		
		
	}
	
	public void PersistentCD(double argSC,WeightDecay argWD){//tielman的PCD训练算法。推荐使用L2
		/* 计算梯度的第二项时不使用当前训练样例，而是沿用前一个状态。
		 * 
		 * */
		int epoch=0;
		while(true){
			double[] v1=this.sample();
			for(int i=0;i<this.dataSet.getDataCount();i++){//开始一轮训练
				this.rbm.clearNodeState(0);
				this.rbm.clearNodeState(1);//打扫干净两层的残留状态
				
				double[] v0=this.dataSet.getData(i);
				this.rbm.setNodeState(v0, 0);//设置可见层的状态为V0
				double[] tempHCV0=new double[this.rbm.hn];//计算隐层的概率
//				for(RBMNode n:this.rbm.Nodes){
//					if(n.getType()==1){
//						tempHCV0[n.getID()]=n.getProbability();
//					}
//				}
				for(RBMNode n:this.rbm.hNodes)
					tempHCV0[n.getID()]=n.getProbability();

				this.rbm.setNodeState(v1, 0);				
				double[] tempHCV1=new double[this.rbm.hn];//计算隐藏层节点的概率
//				for(RBMNode n:this.rbm.Nodes){
//					if(n.getType()==1){
//						tempHCV1[n.getID()]=n.getProbability();
//					}
//				}
				for(RBMNode n:this.rbm.hNodes)
						tempHCV1[n.getID()]=n.getProbability();
				this.rbm.getNodeState(1);//根据概率给隐层赋值
				this.rbm.clearNodeState(0);
				v1=this.rbm.getNodeState(0);//根据概率给显层赋值并返回
				this.rbm.clearNodeState(1);
				
				for(int v=0;v<this.rbm.vn;v++){
					for(int h=0;h<this.rbm.hn;h++){
						double tempDW=(double)v0[v]*tempHCV0[h]-(double)v1[v]*tempHCV1[h];
						if(argWD!=null){
							tempDW+=argWD.getWeightDecay(this.rbm.W, v, h);
						}
						this.rbm.W[v][h]+=this.rate*tempDW;
					}
				}

				this.rbm.UpdateRBM();//更新RBM,矩阵只表示了互连权值，节点的偏移值要另外设置
//				for(RBMNode n:this.rbm.Nodes){//更新节点bias
//					//double tempWD=argWD==null?0:n.bias*argWD.getWeightCost();权值衰减通常不用于节点偏移
//					if(n.getType()==0){//更新可见层的bias
//						n.setBias(n.getBias()+this.rate*((v0[n.getID()]-v1[n.getID()])/n.getVariance()));
//					}else{//更新隐藏层的bias
//						n.setBias(n.getBias()+this.rate*(tempHCV0[n.getID()]-tempHCV1[n.getID()]));
//					}
//				}
				for(RBMNode n:this.rbm.vNodes)//更新节点bias
					n.setBias(n.getBias()+this.rate*((v0[n.getID()]-v1[n.getID()])/n.getVariance()));
				for(RBMNode n:this.rbm.hNodes)//更新节点bias
					n.setBias(n.getBias()+this.rate*(tempHCV0[n.getID()]-tempHCV1[n.getID()]));
			}
			
			epoch++;
			double tempE=this.Errorta();
			System.out.println("第"+epoch+" 次训练，重构误差"+tempE);
			if(tempE<=argSC || epoch >= this.max_try){
				System.out.println("经过"+epoch+" 次训练，RBM 训练结束");
				break;				
			}
			
		}

	}
	
	
	public void MiniBatchCD(double argSC,WeightDecay argWD,int argMBS){
		//使用minibatch的CD1训练
		//要根据minibatch的容量调整学习率，收敛速度确实更快
		//argMBS minibatch的容量
		
		int epoch=0;
		ArrayList<Data> tempD=this.dataSet.splitMiniBatch(argMBS);
		while(true){
			for(Data d:tempD){//开始用一个minibatch训练网络
				double[][] tempDeltaW=new double[this.rbm.vn][this.rbm.hn];//deltaw累加值的临时缓存，一个minibatch完成后用其更新网络权值
				double[] tempDeltaBV=new double[this.rbm.vn];//显层的deltabias累加值
				double[] tempDeltaBH=new double[this.rbm.hn];//隐层的deltabias累加值
				
				for(Case c:d.getDataSet()){//对于minibatch的每一个训练样例，计算deltaw
					this.rbm.clearNodeState(0);
					this.rbm.clearNodeState(1);
					
					double[] v0=c.getTheCase();
					this.rbm.setNodeState(v0, 0);//设置可见层的状态为V0
					double[] tempHCV0=new double[this.rbm.hn];//计算隐层的概率
//					for(RBMNode n:this.rbm.Nodes){
//						if(n.getType()==1){
//							tempHCV0[n.getID()]=n.getProbability();
//						}
//					}
					for(RBMNode n:this.rbm.hNodes)
						tempHCV0[n.getID()]=n.getProbability();
					double[] h0=this.rbm.getNodeState(1);//取样H0;
					this.rbm.clearNodeState(0);//清空可见层
					double[] v1=this.rbm.getNodeState(0);//取样V1
					this.rbm.clearNodeState(1);//清空隐藏层
					double[] tempHCV1=new double[this.rbm.hn];//计算隐藏层节点的概率
//					for(RBMNode n:this.rbm.Nodes){
//						if(n.getType()==1){
//							tempHCV1[n.getID()]=n.getProbability();
//						}
//					}
					for(RBMNode n:this.rbm.hNodes)
						tempHCV1[n.getID()]=n.getProbability();
					
					for(int v=0;v<this.rbm.vn;v++){
						for(int h=0;h<this.rbm.hn;h++){
							tempDeltaW[v][h]+=(double)v0[v]*tempHCV0[h]-(double)v1[v]*tempHCV1[h];//梯度累加
						}
					}
					
//					for(RBMNode n:this.rbm.Nodes){//更新节点bias，note 节点偏移量更新不需要权值衰减
//						if(n.getType()==0){//更新可见层的bias
//							tempDeltaBV[n.getID()]+=v0[n.getID()]-v1[n.getID()];
//						}else{//更新隐藏层的bias
//							tempDeltaBH[n.getID()]+=tempHCV0[n.getID()]-tempHCV1[n.getID()];
//						}
//					}
					for(RBMNode n:this.rbm.vNodes)
						tempDeltaBV[n.getID()]+=v0[n.getID()]-v1[n.getID()];
					for(RBMNode n:this.rbm.hNodes)
						tempDeltaBH[n.getID()]+=tempHCV0[n.getID()]-tempHCV1[n.getID()];
					
				}//一个minibatch的训练样例全部完成
				
				//Tool.PrintW(tempDeltaW);
				//更新网络权值
				for(int v=0;v<this.rbm.vn;v++){
					for(int h=0;h<this.rbm.hn;h++){
						this.rbm.W[v][h]+=(this.rate*(tempDeltaW[v][h])+(argWD==null?0:argWD.getWeightDecay(this.rbm.W, v, h)))/(double)d.getDataCount();
					}
				}
				
				this.rbm.UpdateRBM();//更新RBM,矩阵只表示了互连权值，节点的偏移值要另外设置
//				for(RBMNode n:this.rbm.Nodes){//更新节点bias
//					if(n.getType()==0){//更新可见层的bias
//						n.setBias(n.getBias()+this.rate/(double)d.getDataCount()*(tempDeltaBV[n.getID()]));
//					}else{//更新隐藏层的bias
//						n.setBias(n.getBias()+this.rate/(double)d.getDataCount()*(tempDeltaBH[n.getID()]));
//					}
//				}
				for(RBMNode n:this.rbm.vNodes)
					n.setBias(n.getBias()+this.rate/(double)d.getDataCount()*(tempDeltaBV[n.getID()]));
				for(RBMNode n:this.rbm.hNodes)
					n.setBias(n.getBias()+this.rate/(double)d.getDataCount()*(tempDeltaBH[n.getID()]));
			}
			
			epoch++;
			double tempE=this.Errorta();
			System.out.println("第"+epoch+" 次训练，重构误差"+tempE);
			if(tempE<=argSC || epoch >= this.max_try){
				System.out.println("经过"+epoch+" 次训练，RBM 训练结束");
				break;				
			}
			
		}

	}
	
	public double[] sample(){//从数据集中随机取样
		return this.dataSet.getData(this.r.nextInt(this.dataSet.getDataCount()));
	}
	
	
	
}
