package com.lqyandpy.DBN;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import com.lqyandpy.RBM.*;


public class SimpleDBN {
	public int Layers=2;
	public ArrayList<RBM> RBMStack=new ArrayList<RBM>();//RBMStack(0)����ײ�
	public ArrayList<Double> output_layer;
	public double[][] output_w;
	public double[][] input_w; 
	
	public void constructDBN(int input_num,int[] hidden_nums,boolean gauss)
	{
		this.RBMStack.clear();
		
		RBM tempR=new RBM(input_num,hidden_nums[0],gauss);
		this.RBMStack.add(tempR);
		for(int i = 1;i < hidden_nums.length;++i)
		{
			tempR = new RBM(hidden_nums[i-1],hidden_nums[i],false);
			this.RBMStack.add(tempR);
		}
	}
	public void add_output_layer(int type_sum)
	{
		this.output_layer = new ArrayList<Double>(type_sum);
		this.output_w = new double[type_sum][this.getTop().vn];
		for(int i = 0;i < type_sum;++i)
			for(int j = 0;j < this.getTop().vn;++j)
			{
				double r = Math.random();
				this.output_w[i][j] = r*(-1.0d)+(1-r)*1.0d;
			}
	}
	
	public void init_input_weight(int inputlen,int layerlen)
	{
		this.input_w = new double[layerlen][inputlen];
		for(int i = 0;i < layerlen;++i)
			for(int j = 0;j < inputlen;++j)
			{
				double r = Math.random();
				this.output_w[i][j] = r*(-1.0d)+(1-r)*1.0d;
			}
	}
	
	public void InsertRBM(RBM tempR){
		this.RBMStack.add(tempR);
	}
	
	public RBM getTop(){
		return this.RBMStack.get(this.RBMStack.size()-1);
	}
	
	public RBM getRBM(int argI){
		return this.RBMStack.get(argI<this.RBMStack.size()&&argI>=0?argI:this.RBMStack.size()-1);
	}
	
	public void PermanentDBN(String argS){
		try{
			FileOutputStream tempFO=new FileOutputStream(argS);
			ObjectOutputStream tempOO = new ObjectOutputStream(tempFO);
			
			ArrayList<PermanentRBM> tempPR=new ArrayList<PermanentRBM>();
			for(RBM r:this.RBMStack){
				tempPR.add(r.SaveAS());
			}
			
			tempOO.writeObject(tempPR);

			tempOO.close();
			
        }catch(Exception e){
        	e.printStackTrace();
        }	
	}
	
	public void RebuildDBN(String argS){
		ArrayList<RBM> tempL=new ArrayList<RBM>();
		File tempF=new File(argS);
		
		try{
			FileInputStream tempFI=new FileInputStream(argS);
			ObjectInputStream tempOI=new ObjectInputStream(tempFI);
			ArrayList<PermanentRBM> tempPRL = (ArrayList<PermanentRBM>)tempOI.readObject();
			
			for(PermanentRBM pr:tempPRL){
				tempL.add(pr.ReBuildRBM());
			}
			tempOI.close();
		}catch(Exception e){
			e.printStackTrace();
		} 
		
		this.Layers=tempL.size();
		this.RBMStack=tempL;
	}
	
	private int find_layer(int[] bin,int key)
	{
		for(int i = 0;i < bin.length;++i)
			if(bin[i] > key)
				return i;
		return -1;	
	}
	public int[] ann_bias;
	public double[][] get_ann_wight(int out_num)
	{
		
		int[] node_bin = new int[this.RBMStack.size()+2];//node_bin�е�i��Ԫ�ر�ʾ��i�㣨���ϵ��£�������㵽����㣩�ڵ��ŵĺ�һ����
		int layer_sum = this.RBMStack.size()+2;
		node_bin[0] = out_num;
		for(int i = 0;i < this.RBMStack.size() ;++i)
		{
			node_bin[i+1] = node_bin[i] + this.RBMStack.get(this.RBMStack.size()-1-i).hn+1;
		}
		node_bin[this.RBMStack.size()+1] = node_bin[this.RBMStack.size()] + this.RBMStack.get(0).vn + 1;
		int node_sum = node_bin[layer_sum-1];
		double[][] tempW = new double[node_sum][node_sum];
		for(int i = 0;i < node_sum;++i)
			for(int j = 0;j < node_sum;++j)
				tempW[i][j] = Double.NaN;
		for(int i = 0;i < node_sum;++i)
		{
			int layer = find_layer(node_bin,i);
			if(layer == -1)
				return tempW;
			if(i == node_bin[layer]-1)
				continue;
			else if(layer == 0)
			{
				for(int j = node_bin[0];j < node_bin[1];++j)
				{//ƫ�ƺͷ�ƫ�ƽڵ綼һ��,����ʼ��
					double r = Math.random();
					tempW[i][j] = r;
					tempW[j][i] = r;
				}
			}
			else if(layer < layer_sum-1)
			{
				for(int j = node_bin[layer];j < node_bin[layer+1]-1;++j)
				{//��ƫ�ƽ��
					RBM i_rbm = this.RBMStack.get(this.RBMStack.size() - layer);
					double[][] w = i_rbm.W;
					double r = w[j-node_bin[layer]][i-node_bin[layer-1]];
					tempW[i][j] = r;
					tempW[j][i] = r;
				}
				//ƫ�ƽ��
				double r = this.RBMStack.get(this.RBMStack.size() - layer).hNodes.get(i-node_bin[layer-1]).getBias();
				tempW[i][node_bin[layer+1]-1] = r;
				tempW[node_bin[layer+1]-1][i] = r;
			}
		}
		this.ann_bias = new int[layer_sum-1];
		for(int i = 1;i < layer_sum;++i)
			this.ann_bias[i-1] = node_bin[i]-1;
		return tempW;
	}
}
