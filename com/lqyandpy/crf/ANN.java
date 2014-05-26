package com.lqyandpy.crf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class ANN implements Serializable{
	public ArrayList<Node> Nodes=new ArrayList<Node>();//
	
	public int NetworkSize;
	
	public static byte[] serialize(ANN ann)
	{
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(ann);
			oos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static ANN deserialize(byte[] bytes)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			ANN ann = (ANN)ois.readObject();
			return ann;
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void combineANN(ANN ann)
	{
		if(this.NetworkSize != ann.NetworkSize)
			return;
		for(int i = 0;i < this.Nodes.size();++i)
		{
			ArrayList<Link> thislist = this.Nodes.get(i).getLinks();
			ArrayList<Link> thatlist = ann.Nodes.get(i).getLinks();
			for(int j = 0;j < thislist.size();++j)
			{
				thislist.get(i).Weight = (thislist.get(i).Weight + thatlist.get(i).Weight) / 2.0;
			}
		}
	}
	
	public ArrayList<InputNode> getInputNodes(){//��������б�
		ArrayList<InputNode> tempN=new ArrayList<InputNode>();
		for(Node n:Nodes){
			if(n.getNodeType()==3){
				tempN.add((InputNode)n);
			}
		}
		Collections.sort(tempN);
		return tempN;
	}
	
	public ArrayList<OutputNode> getOutputNodes(){//�����Ԫ�б�
		ArrayList<OutputNode> tempN=new ArrayList<OutputNode>();
		for(Node n:Nodes){
			if(n.getNodeType()==1){
				tempN.add((OutputNode)n);
			}
		}
		Collections.sort(tempN);
		return tempN;
	}
	
	public ArrayList<HiddenNode> getHiddenNodes(){//������Ԫ�б�
		ArrayList<HiddenNode> tempN=new ArrayList<HiddenNode>();
		for(Node n:Nodes){
			if(n.getNodeType()==2){
				tempN.add((HiddenNode)n);
			}
		}
		Collections.sort(tempN);
		return tempN;
	}
	
	public double[] getOutput(double[] argD){//�����������ֵ
		ArrayList<OutputNode> tempONS=this.getOutputNodes();
		double[] tempR=new double[tempONS.size()];//׼�����
		
		ArrayList<InputNode> tempINS=this.getInputNodes();//
		
		//assert(argD.length==tempINS.size());
		for(int i=0;i<argD.length;i++){
			tempINS.get(i).Value=argD[i];
		}//Ϊ����ڵ㸳ֵ
		
		int i=0;
		for(OutputNode on:tempONS){
			tempR[i]=on.getOutput();
			i++;
		}
		
		return tempR;
	}
	
	public ArrayList<Node> getNodesFrom(int argI){
		return this.getNodesFrom(this.getNode(argI));
	}
	
	public ArrayList<Node> getNodesFrom(Node argN){
		ArrayList<Node> tempL=new ArrayList<Node>();
		switch(argN.getNodeType()){
		case(1):break;//����ڵ㣬û�д������������
		case(2):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//����ڵ㣬�д������������
		case(3):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//�����
		case(4):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//ƫ��
		default:break;
		}
		
		return tempL;
	}
	
	public void InitAnn(double[][] argD,int[] argB,ActivationFunction argHF,ActivationFunction argOF){//����һ����������ṹ�ͳ�ʼȨֵ.����߲㿪ʼ��ţ�Խ���±��Խ��
		//argB bias�ڵ���
		this.NetworkSize=argD[0].length;
		
		for(int i=0;i<this.NetworkSize;i++){
			switch(this.NodeType(i, argD[i],argB)){
			case(1)://�½�����ڵ�
				OutputNode tempON=new OutputNode();
				tempON.id=i;
				tempON.setActivateFunction(argOF);
				Nodes.add(tempON);
				System.out.println("�½�����ڵ� "+tempON.id);
			break;
			case(2)://�½�����ڵ�
				HiddenNode tempHN=new HiddenNode();
				tempHN.id=i;
				tempHN.setActivateFunction(argHF);
				Nodes.add(tempHN);
				System.out.println("�½�����ڵ� "+tempHN.id);
			break;
			case(3)://�½�����ڵ�
				InputNode tempIN=new InputNode();
				tempIN.id=i;
				Nodes.add(tempIN);
				System.out.println("�½�����ڵ� "+tempIN.id);
			break;
			case(4)://�½�bias�ڵ�
				BiasNode tempBN=new BiasNode();
				tempBN.id=i;
				Nodes.add(tempBN);
				System.out.println("�½�ƫ�ƽڵ� "+tempBN.id);
			break;
			default:break;
			}
		}
		
		for(Node n:Nodes){
			int tempID=n.getID();
			double[] tempWV=argD[tempID];//��ڵ������Ȩֵ����
			
			switch(n.getNodeType()){
			case(1):
			OutputNode tempON=(OutputNode)n;
			for(int i=0;i<tempWV.length;i++){
				if(!new Double(tempWV[i]).equals(Double.NaN)){
					
					Link tempL=new Link();
					tempL.Weight=tempWV[i];
					tempL.To=tempON;
					tempL.From=this.getNode(i);
					
					tempON.Links.add(tempL);
				}
			}
			break;
			case(2):
			HiddenNode tempHN=(HiddenNode)n;
			
			for(int i=0;i<tempWV.length;i++){
				if(i>n.getID()&&!new Double(tempWV[i]).equals(Double.NaN)){
					
					Link tempL=new Link();
					tempL.Weight=tempWV[i];
					tempL.To=tempHN;
					tempL.From=this.getNode(i);
					
					tempHN.Links.add(tempL);
					
				}
			}
			
			break;
			case(3):
			break;
			case(4):
			break;
			default:break;
			}
			
			
		}
		
	}
	
	public Node getNode(int argID){
		Node tempN=null;
		for(Node n:Nodes){
			if(n.getID()==argID){
				tempN=n;
			}
		}
		return tempN;
	}
	
	
	
	public double[][] ConstructInitMatrix(int[][] argM){//ͨ�ó�ʼ����
		/*  1,2,n,n,n,n
		 *  3,4,5,6,n,n
		 *  7,8,9,n,n,n
		 *  10,11,12,13,n,n
		 */
		int tempD=0;
		
		for(int i=0;i<argM.length;i++)
			for(int j=0;j<argM[0].length;j++){
				if(argM[i][j]>tempD)
					tempD=argM[i][j];
			}
		tempD++;
		
		double[][] tempR=new double[tempD][tempD];
		
		for(int i=0;i<tempD;i++)
			for(int j=0;j<tempD;j++)
				tempR[i][j]=Double.POSITIVE_INFINITY;
		
		for(int i=0;i<tempD;i++){
			for(int j=0;j<tempD;j++){
				if(i==j){//û��������
					tempR[i][j]=Double.NaN;
				}else{
					if(new Double(tempR[i][j]).equals(Double.POSITIVE_INFINITY)){//���û�и�ֵ
						int tempI=this.LayerCompare(i, j, argM);
						if((Math.abs(tempI)>1)||(tempI==0)){//��Խ�㼶�Ľڵ�������,ͬһ��ڵ�֮��������
							tempR[i][j]=tempR[j][i]=Double.NaN;
						}else{
							double r=Math.random();
							tempR[i][j]=tempR[j][i]=r*(-1.0d)+(1-r)*1.0d;
						}
					}
				}
				
			}
		}
		
		return tempR;
		
	}
	
	/*���ṹ��ר�ó�ʼ����*/
	public double[][] ConstructInitMatrix(int argI,int[] argOL,int[] argHL,int[] argIL){
		
		double[][] tempR=new double[argI][argI];
		
		for(int i=0;i<argI;i++)
			for(int j=0;j<argI;j++)
				tempR[i][j]=Double.POSITIVE_INFINITY;
		
		for(int i=0;i<argI;i++){
			for(int j=0;j<argI;j++){
				if(i==j){//û��������
					tempR[i][j]=Double.NaN;
				}else{
					if(new Double(tempR[i][j]).equals(Double.POSITIVE_INFINITY)){//���û�и�ֵ
						int tempI=this.LayerCompare(i, j, argOL, argHL, argIL);
						if((Math.abs(tempI)>1)||(tempI==0)){//��Խ�㼶�Ľڵ�������,ͬһ��ڵ�֮��������
							tempR[i][j]=tempR[j][i]=Double.NaN;
						}else{
							tempR[i][j]=tempR[j][i]=Math.random();
						}
					}
				}
				
			}
		}
		
		return tempR;
		
	}
	
	private int LayerCompare(int argI,int argJ,int[][] argM){
		return this.getIndexLayer(argI, argM)-this.getIndexLayer(argJ, argM);
	}
	
	private int getIndexLayer(int argI,int[][] argM){
		
		int tempI=-1;
		
		for(int i=0;i<argM.length;i++){
			boolean found=false;
			for(int j=0;j<argM[0].length;j++){
				if(argM[i][j]==argI){
					tempI=i;
					found = true;
					break;
				}
			}
			if(found){break;}
		}
		
		return tempI;
		
	}
	
	private int LayerCompare(int argI,int argJ,int[] argOL,int[] argHL,int[] argIL){//���ؽڵ�i�ͽڵ�j���ڲ�εĲ�ֵ
		return this.getIndexLayer(argI, argOL, argHL, argIL)-this.getIndexLayer(argJ, argOL, argHL, argIL);
	}
	
	private int getIndexLayer(int argI,int[] argOL,int[] argHL,int[] argIL){
		int tempI=-1;
		
		if(this.InArray(argI, argOL)){
			tempI=1;
		}else if(this.InArray(argI, argHL)){
			tempI=2;
		}else if(this.InArray(argI, argIL)){
			tempI=3;
		}
		
		return tempI;
	}
	
	private boolean InArray(int argI,int[] argA){
		boolean tempB=false;
		for(int i:argA){
			if(argI==i){
				tempB=true;
				break;
			}
		}
		return tempB;
	}
	
	public double[][] getWeightMatrix(){
		double[][] tempR=new double[this.NetworkSize][this.NetworkSize];
		
		for(int i=0;i<tempR.length;i++)
			for(int j=0;j<tempR[0].length;j++){
				tempR[i][j]=Double.NaN;
			}
		
		for(Node n:this.Nodes){
			for(Link l:n.getLinks()){
				tempR[n.getID()][l.From.getID()]=tempR[l.From.getID()][n.getID()]=l.Weight;
				
			}
		}
		
		return tempR;
	}
	
	
	private int NodeType(int argI,double[] argD,int argB[]){//argI �ڵ��ţ�argD argI���ڽ�����
		int minIndex=0;
		int maxIndex=0;
		
		int res=-1;
		
		for(int i=0;i<argB.length;i++){//���ȼ���Ƿ�bias�ڵ�
			if(argI==argB[i]){
				return 4;
			}
		}
		
		for(int i=0;i<argD.length;i++){
			if(!new Double(argD[i]).equals(Double.NaN)){
				minIndex=i;
				break;
			}
		}
			
		for(int i=argD.length-1;i>-1;i--){
			if(!new Double(argD[i]).equals(Double.NaN)){
				maxIndex=i;
				break;
			}
		}
		
		if(maxIndex-argI>0&&minIndex-argI>0){
			res=1;//����ڵ�
		}else if(maxIndex-argI>0&&minIndex-argI<0){
			res=2;//���ؽڵ�
		}else if(maxIndex-argI<0&&minIndex-argI<0){
			res=3;//����ڵ�
		}
		
	//	System.out.println("�ڵ���:"+argI+" min��"+minIndex+" max:"+maxIndex+" res:"+res);
		
		return res;
	}
	

}
