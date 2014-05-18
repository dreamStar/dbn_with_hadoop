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
	public ArrayList<RBM> RBMStack=new ArrayList<RBM>();//RBMStack(0)ÊÇ×îµ×²ã
	public ArrayList<Double> output_layer;
	public double[][] output_w;
	public double[][] input_w; 
	
	
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
	
	
}
