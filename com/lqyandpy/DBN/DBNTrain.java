package com.lqyandpy.DBN;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import com.lqyandpy.RBM.CDTrain;
import com.lqyandpy.RBM.Data;
import com.lqyandpy.RBM.Data.Case;
import com.lqyandpy.RBM.RBM;
import com.lqyandpy.RBM.BasicRBMNode;
import com.lqyandpy.RBM.WeightDecay;


public class DBNTrain {
	//public double learningrate;
	private Data dataSet;
	private SimpleDBN dbn;
	private double rate=0.001;
	
	
	//public Weight_Decay wd;
	
	
	public DBNTrain(Data argD,SimpleDBN argN){
		this.dataSet=argD;
		this.dbn=argN;
	}
	
	public void setLearningRate(double argD){
		this.rate=argD;
	}
	
	public void greedyLayerwiseTraining(double argSC,double argLR,int argH,WeightDecay argWD,boolean argG,int max_try,int batch_size){//停止条件,学习率,隐含节点的数目,权值衰减策略
		RBM tempR=new RBM(this.dataSet.getDimension(),argH,argG);
		CDTrain tempCDT=new CDTrain(dataSet, tempR,max_try);
		if(batch_size == 1)
			tempCDT.PersistentCD(argSC,argWD);
		else
			tempCDT.MiniBatchCD(argSC, argWD, batch_size);
		//this.dbn.InsertRBM(tempR);
		this.greedyLayerwiseTraining(tempR, argSC,argLR,argWD,max_try,batch_size);
	}
	
	public void greedyLayerwiseTraining(RBM argSeed,double argSC,double argLR,WeightDecay argWD,int max_try,int batch_size){//第一层RBM，停止条件，学习率，权值衰减策略
		this.dbn.InsertRBM(argSeed);
		RBM tempR=argSeed.CopyTiedRBM();//第一层已经训练过了，直接开始训练第二层
		for(int i=1;i<this.dbn.Layers;i++){
			Data tempD=this.getDataForNextLayer();//取样本层的训练数据
			CDTrain tempCDT=new CDTrain(tempD,tempR,max_try);
			//tempCDT.PersistentCD(argSC,argWD);//训练本层RBM
			if(batch_size == 1)
				tempCDT.PersistentCD(argSC,argWD);
			else
				tempCDT.MiniBatchCD(argSC, argWD, batch_size);
			
			
			System.out.println("完成第"+(i+1)+"层训练");
			
			this.dbn.InsertRBM(tempR);//将训练好的RBM放入栈中
			tempR=tempR.CopyTiedRBM();//取得下一层原始RBM
		}
	}
	
	public Data getDataForNextLayer(){//从当前的RBM堆中取样，作为下一层RBM的训练输入
		double[][] tempDB=new double[this.dataSet.getDataCount()][this.dbn.getTop().hn];
		int tempC=0;
		for(Case v:this.dataSet.getDataSet()){
			double[] tempI=v.getTheCase();
			for(RBM m:this.dbn.RBMStack){
				m.clearNodeState(0);
				m.clearNodeState(1);
				
				m.setNodeState(tempI,0);
				tempI=m.getNodeState(1);
				
				m.clearNodeState(0);
				m.clearNodeState(1);
				
				
			}
			tempDB[tempC++]=tempI;
		}
		
		return new Data(tempDB,false);//隐层都是二值的，不需要规范化
	}

	
}
