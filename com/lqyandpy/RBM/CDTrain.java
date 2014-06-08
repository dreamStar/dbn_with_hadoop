package com.lqyandpy.RBM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.lqyandpy.DBN.DBNTrain;
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
		for(RBMNode n:this.rbm.vNodes){//��ѵ����ݳ�ʼ��bias
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
		System.out.println("--------------------------------�������----------------------------------");
		double tempE=0;
		
		for(Case c:this.dataSet.getDataSet()){
			this.rbm.clearNodeState(0);
			this.rbm.clearNodeState(1);
			
			double[] v=c.getTheCase();
			this.rbm.setNodeState(v, 0);//Ϊ�ɼ�ڵ㸳ֵclamp
			double[] h0=this.rbm.getNodeState(1);//��������ڵ�ȡ��
			this.rbm.clearNodeState(0);//��տɼ��
			double[] v1=this.rbm.getNodeState(0);//�Կɼ��ȡ��
			this.rbm.clearNodeState(1);//��������㡣
			
			double tempForm=0;
			for(int i=0;i<v.length;i++){
				tempForm+=Math.pow((double)v[i]-v1[i], 2);
			}

			tempE+=Math.sqrt(tempForm);
		}
		
		return tempE;
	}
	
	public void CD(double argSC,WeightDecay argWD){//��CD��REͣ��������L2��L1
		int epoch=0;
		while(true){
			for(int i=0;i<this.dataSet.getDataCount();i++){//��ʼһ��ѵ��
				this.rbm.clearNodeState(0);
				this.rbm.clearNodeState(1);//��ɨ�ɾ�����Ĳ���״̬

				double[] v0=this.dataSet.getData(i);
				this.rbm.setNodeState(v0, 0);//���ÿɼ���״̬ΪV0
				double[] tempHCV0=new double[this.rbm.hn];//��������ĸ���
//				for(RBMNode n:this.rbm.Nodes){
//					if(n.getType()==1){
//						tempHCV0[n.getID()]=n.getProbability();
//					}
//				}
				for(RBMNode n:this.rbm.hNodes)
						tempHCV0[n.getID()]=n.getProbability();
				double[] h0=this.rbm.getNodeState(1);//ȡ��H0;
				this.rbm.clearNodeState(0);//��տɼ��
				double[] v1=this.rbm.getNodeState(0);//ȡ��V1
				this.rbm.clearNodeState(1);//������ز�
				double[] tempHCV1=new double[this.rbm.hn];//�������ز�ڵ�ĸ���
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

				//this.rbm.UpdateRBM();//����RBM,����ֻ��ʾ�˻���Ȩֵ���ڵ��ƫ��ֵҪ��������
//				for(RBMNode n:this.rbm.Nodes){//���½ڵ�bias
//					if(n.getType()==0){//���¿ɼ���bias
//						n.setBias(n.getBias()+this.rate*((v0[n.getID()]-v1[n.getID()])/n.getVariance()));
//					}else{//�������ز��bias
//						n.setBias(n.getBias()+this.rate*(tempHCV0[n.getID()]-tempHCV1[n.getID()]));
//					}
//				}
				for(RBMNode n:this.rbm.vNodes)
					n.setBias(n.getBias()+this.rate*((v0[n.getID()]-v1[n.getID()])/n.getVariance()));
				for(RBMNode n:this.rbm.hNodes)
					n.setBias(n.getBias()+this.rate*(tempHCV0[n.getID()]-tempHCV1[n.getID()]));
			}
			
			epoch++;
//			double tempE=this.Errorta();
//			System.out.println("��"+epoch+" ��ѵ�����ع����"+tempE);
//			if(tempE<=argSC){
//				System.out.println("����"+epoch+" ��ѵ����RBM ����");
//				break;				
//			}
			
		}
		
		
		
		
	}
	
	public void PersistentCD(double argSC,WeightDecay argWD){//tielman��PCDѵ���㷨���Ƽ�ʹ��L2
		/* �����ݶȵĵڶ���ʱ��ʹ�õ�ǰѵ�������������ǰһ��״̬��
		 * 
		 * */
		int epoch=0;
		while(true){
			double[] v1=this.sample();
			for(int i=0;i<this.dataSet.getDataCount();i++){//��ʼһ��ѵ��
				this.rbm.clearNodeState(0);
				this.rbm.clearNodeState(1);//��ɨ�ɾ�����Ĳ���״̬
				
				double[] v0=this.dataSet.getData(i);
				this.rbm.setNodeState(v0, 0);//���ÿɼ���״̬ΪV0
				double[] tempHCV0=new double[this.rbm.hn];//��������ĸ���
//				for(RBMNode n:this.rbm.Nodes){
//					if(n.getType()==1){
//						tempHCV0[n.getID()]=n.getProbability();
//					}
//				}
				for(RBMNode n:this.rbm.hNodes)
					tempHCV0[n.getID()]=n.getProbability();

				this.rbm.setNodeState(v1, 0);				
				double[] tempHCV1=new double[this.rbm.hn];//�������ز�ڵ�ĸ���
//				for(RBMNode n:this.rbm.Nodes){
//					if(n.getType()==1){
//						tempHCV1[n.getID()]=n.getProbability();
//					}
//				}
				for(RBMNode n:this.rbm.hNodes)
						tempHCV1[n.getID()]=n.getProbability();
				this.rbm.getNodeState(1);//��ݸ��ʸ����㸳ֵ
				this.rbm.clearNodeState(0);
				v1=this.rbm.getNodeState(0);//��ݸ��ʸ��Բ㸳ֵ������
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

				//this.rbm.UpdateRBM();//����RBM,����ֻ��ʾ�˻���Ȩֵ���ڵ��ƫ��ֵҪ��������
//				for(RBMNode n:this.rbm.Nodes){//���½ڵ�bias
//					//double tempWD=argWD==null?0:n.bias*argWD.getWeightCost();Ȩֵ˥��ͨ�������ڽڵ�ƫ��
//					if(n.getType()==0){//���¿ɼ���bias
//						n.setBias(n.getBias()+this.rate*((v0[n.getID()]-v1[n.getID()])/n.getVariance()));
//					}else{//�������ز��bias
//						n.setBias(n.getBias()+this.rate*(tempHCV0[n.getID()]-tempHCV1[n.getID()]));
//					}
//				}
				for(RBMNode n:this.rbm.vNodes)//���½ڵ�bias
					n.setBias(n.getBias()+this.rate*((v0[n.getID()]-v1[n.getID()])/n.getVariance()));
				for(RBMNode n:this.rbm.hNodes)//���½ڵ�bias
					n.setBias(n.getBias()+this.rate*(tempHCV0[n.getID()]-tempHCV1[n.getID()]));
			}
			
			epoch++;
//			double tempE=this.Errorta();
//			System.out.println("��"+epoch+" ��ѵ�����ع����"+tempE);
//			if(tempE<=argSC || epoch >= this.max_try){
//				System.out.println("����"+epoch+" ��ѵ����RBM ѵ������");
//				break;				
//			}
			
		}

	}
	
	
	public void MiniBatchCD(double argSC,WeightDecay argWD,int argMBS){
		//ʹ��minibatch��CD1ѵ��
		//Ҫ���minibatch����������ѧϰ�ʣ������ٶ�ȷʵ���
		//argMBS minibatch������
		
		int epoch=0;
		ArrayList<Data> tempD=this.dataSet.splitMiniBatch(argMBS);
		
		while(true){
			DBNTrain.print_time("pretrain for epoch " + epoch);
			for(Data d:tempD){//��ʼ��һ��minibatchѵ������
				//DBNTrain.print_time("prepare data for a batch");
				double[][] tempDeltaW=new double[this.rbm.vn][this.rbm.hn];//deltaw�ۼ�ֵ����ʱ���棬һ��minibatch��ɺ������������Ȩֵ
				double[] tempDeltaBV=new double[this.rbm.vn];//�Բ��deltabias�ۼ�ֵ
				double[] tempDeltaBH=new double[this.rbm.hn];//�����deltabias�ۼ�ֵ
				
				for(Case c:d.getDataSet()){//����minibatch��ÿһ��ѵ���������deltaw
					//DBNTrain.print_time("train a single case");
					this.rbm.clearNodeState(0);
					this.rbm.clearNodeState(1);
					
					double[] v0=c.getTheCase();
					this.rbm.setNodeState(v0, 0);//���ÿɼ���״̬ΪV0
					double[] tempHCV0=new double[this.rbm.hn];//��������ĸ���
//					for(RBMNode n:this.rbm.Nodes){
//						if(n.getType()==1){
//							tempHCV0[n.getID()]=n.getProbability();
//						}
//					}
					for(RBMNode n:this.rbm.hNodes)
						tempHCV0[n.getID()]=n.getProbability();
					double[] h0=this.rbm.getNodeState(1);//ȡ��H0;
					this.rbm.clearNodeState(0);//��տɼ��
					double[] v1=this.rbm.getNodeState(0);//ȡ��V1
					this.rbm.clearNodeState(1);//������ز�
					double[] tempHCV1=new double[this.rbm.hn];//�������ز�ڵ�ĸ���
//					for(RBMNode n:this.rbm.Nodes){
//						if(n.getType()==1){
//							tempHCV1[n.getID()]=n.getProbability();
//						}
//					}
					for(RBMNode n:this.rbm.hNodes)
						tempHCV1[n.getID()]=n.getProbability();
					
					for(int v=0;v<this.rbm.vn;v++){
						for(int h=0;h<this.rbm.hn;h++){
							tempDeltaW[v][h]+=(double)v0[v]*tempHCV0[h]-(double)v1[v]*tempHCV1[h];//�ݶ��ۼ�
						}
					}
					
//					for(RBMNode n:this.rbm.Nodes){//���½ڵ�bias��note �ڵ�ƫ�������²���ҪȨֵ˥��
//						if(n.getType()==0){//���¿ɼ���bias
//							tempDeltaBV[n.getID()]+=v0[n.getID()]-v1[n.getID()];
//						}else{//�������ز��bias
//							tempDeltaBH[n.getID()]+=tempHCV0[n.getID()]-tempHCV1[n.getID()];
//						}
//					}
					for(RBMNode n:this.rbm.vNodes)
						tempDeltaBV[n.getID()]+=v0[n.getID()]-v1[n.getID()];
					for(RBMNode n:this.rbm.hNodes)
						tempDeltaBH[n.getID()]+=tempHCV0[n.getID()]-tempHCV1[n.getID()];
					
				}//һ��minibatch��ѵ������ȫ�����
				
				//Tool.PrintW(tempDeltaW);
				//��������Ȩֵ
				//DBNTrain.print_time("update all Weight");
				for(int v=0;v<this.rbm.vn;v++){
					for(int h=0;h<this.rbm.hn;h++){
						this.rbm.W[v][h]+=(this.rate*(tempDeltaW[v][h])+(argWD==null?0:argWD.getWeightDecay(this.rbm.W, v, h)))/(double)d.getDataCount();
					}
				}
				
				//this.rbm.UpdateRBM();//����RBM,����ֻ��ʾ�˻���Ȩֵ���ڵ��ƫ��ֵҪ��������
//				for(RBMNode n:this.rbm.Nodes){//���½ڵ�bias
//					if(n.getType()==0){//���¿ɼ���bias
//						n.setBias(n.getBias()+this.rate/(double)d.getDataCount()*(tempDeltaBV[n.getID()]));
//					}else{//�������ز��bias
//						n.setBias(n.getBias()+this.rate/(double)d.getDataCount()*(tempDeltaBH[n.getID()]));
//					}
//				}
				//DBNTrain.print_time("update all bias");
				for(RBMNode n:this.rbm.vNodes)
					n.setBias(n.getBias()+this.rate/(double)d.getDataCount()*(tempDeltaBV[n.getID()]));
				for(RBMNode n:this.rbm.hNodes)
					n.setBias(n.getBias()+this.rate/(double)d.getDataCount()*(tempDeltaBH[n.getID()]));
			}
			
			epoch++;
//			DBNTrain.print_time("calculate the error");
//			double tempE=this.Errorta();
//			
//			System.out.println("��"+epoch+" ��ѵ�����ع����"+tempE);
//			if(tempE<=argSC || epoch >= this.max_try){
//				System.out.println("����"+epoch+" ��ѵ����RBM ѵ������");
//				break;				
//			}
			
		}

	}
	
	public double[] sample(){//����ݼ������ȡ��
		return this.dataSet.getData(this.r.nextInt(this.dataSet.getDataCount()));
	}
	
	
	
}
