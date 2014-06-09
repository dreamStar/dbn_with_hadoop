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
	public ArrayList<InputNode> inputNodes = new ArrayList<InputNode>();
	public ArrayList<OutputNode> outputNodes = new ArrayList<OutputNode>();
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
	
	public void clearNodes()
	{
		for(int i = 0;i < this.outputNodes.size();++i)
			this.outputNodes.get(i).clearNodeRecursive();
	}
	
	public static ANN deserialize(byte[] bytes)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			ANN ann = (ANN)ois.readObject();
			return ann;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
	
	public ArrayList<InputNode> getInputNodes(){//锟斤拷锟斤拷锟斤拷锟斤拷斜锟�
		//ArrayList<InputNode> tempN=new ArrayList<InputNode>();
		if(this.inputNodes.isEmpty())
		{
			for (Node n : Nodes) {
				if (n.getNodeType() == 3) {
					this.inputNodes.add((InputNode) n);
				}
			}
			Collections.sort(this.inputNodes);
		}
		return this.inputNodes;
	}
	
	public ArrayList<OutputNode> getOutputNodes(){//锟斤拷锟斤拷锟皆拷斜锟�
		if(!this.outputNodes.isEmpty())
			return this.outputNodes;
		//ArrayList<OutputNode> tempN=new ArrayList<OutputNode>();
		for(Node n:Nodes){
			if(n.getNodeType()==1){
				this.outputNodes.add((OutputNode)n);
			}
		}
		Collections.sort(this.outputNodes);
		return this.outputNodes;
	}
	
	public ArrayList<HiddenNode> getHiddenNodes(){//锟斤拷锟斤拷锟斤拷元锟叫憋拷
		ArrayList<HiddenNode> tempN=new ArrayList<HiddenNode>();
		for(Node n:Nodes){
			if(n.getNodeType()==2){
				tempN.add((HiddenNode)n);
			}
		}
		Collections.sort(tempN);
		return tempN;
	}
	
	public double[] getOutput(double[] argD){//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街�
		//Trainer.print_time("get output nodes");
		ArrayList<OutputNode> tempONS=this.getOutputNodes();
		double[] tempR=new double[tempONS.size()];//准锟斤拷锟斤拷锟�
		//Trainer.print_time("get input nodes");
		ArrayList<InputNode> tempINS=this.getInputNodes();//
		
		//assert(argD.length==tempINS.size());
		
		for(int i=0;i<argD.length;i++){
			tempINS.get(i).Value=argD[i];
		}//为锟斤拷锟斤拷诘愀持�
		
		//Trainer.print_time("get output value");
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
		case(1):break;//锟斤拷锟斤拷诘悖伙拷写锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		case(2):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//锟斤拷锟斤拷诘悖拷写锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
		case(3):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//锟斤拷锟斤拷锟�
		case(4):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//偏锟斤拷
		default:break;
		}
		
		return tempL;
	}
	
	public void InitAnn(double[][] argD,int[] argB,ActivationFunction argHF,ActivationFunction argOF){//锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷峁癸拷统锟绞既ㄖ�.锟斤拷锟斤拷卟憧硷拷锟脚ｏ拷越锟斤拷锟铰憋拷锟皆斤拷锟�
		//argB bias锟节碉拷锟斤拷
		this.NetworkSize=argD[0].length;
		
		for(int i=0;i<this.NetworkSize;i++){
			switch(this.NodeType(i, argD[i],argB)){
			case(1)://锟铰斤拷锟斤拷锟斤拷诘锟�
				OutputNode tempON=new OutputNode();
				tempON.id=i;
				tempON.setActivateFunction(argOF);
				Nodes.add(tempON);
				System.out.println("锟铰斤拷锟斤拷锟斤拷诘锟� "+tempON.id);
			break;
			case(2)://锟铰斤拷锟斤拷锟斤拷诘锟�
				HiddenNode tempHN=new HiddenNode();
				tempHN.id=i;
				tempHN.setActivateFunction(argHF);
				Nodes.add(tempHN);
				System.out.println("锟铰斤拷锟斤拷锟斤拷诘锟� "+tempHN.id);
			break;
			case(3)://锟铰斤拷锟斤拷锟斤拷诘锟�
				InputNode tempIN=new InputNode();
				tempIN.id=i;
				Nodes.add(tempIN);
				System.out.println("锟铰斤拷锟斤拷锟斤拷诘锟� "+tempIN.id);
			break;
			case(4)://锟铰斤拷bias锟节碉拷
				BiasNode tempBN=new BiasNode();
				tempBN.id=i;
				Nodes.add(tempBN);
				System.out.println("锟铰斤拷偏锟狡节碉拷 "+tempBN.id);
			break;
			default:break;
			}
		}
		
		for(Node n:Nodes){
			int tempID=n.getID();
			double[] tempWV=argD[tempID];//锟斤拷诘锟斤拷锟斤拷锟斤拷权值锟斤拷锟斤拷
			
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
	
	
	
	public double[][] ConstructInitMatrix(int[][] argM){//通锟矫筹拷始锟斤拷锟斤拷
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
				if(i==j){//没锟斤拷锟斤拷锟斤拷锟斤拷
					tempR[i][j]=Double.NaN;
				}else{
					if(new Double(tempR[i][j]).equals(Double.POSITIVE_INFINITY)){//锟斤拷锟矫伙拷懈锟街�
						int tempI=this.LayerCompare(i, j, argM);
						if((Math.abs(tempI)>1)||(tempI==0)){//锟斤拷越锟姐级锟侥节碉拷锟斤拷锟斤拷锟斤拷,同一锟斤拷诘锟街拷锟斤拷锟斤拷锟斤拷锟�
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
	
	/*锟斤拷锟结构锟斤拷专锟矫筹拷始锟斤拷锟斤拷*/
	public double[][] ConstructInitMatrix(int argI,int[] argOL,int[] argHL,int[] argIL){
		
		double[][] tempR=new double[argI][argI];
		
		for(int i=0;i<argI;i++)
			for(int j=0;j<argI;j++)
				tempR[i][j]=Double.POSITIVE_INFINITY;
		
		for(int i=0;i<argI;i++){
			for(int j=0;j<argI;j++){
				if(i==j){//没锟斤拷锟斤拷锟斤拷锟斤拷
					tempR[i][j]=Double.NaN;
				}else{
					if(new Double(tempR[i][j]).equals(Double.POSITIVE_INFINITY)){//锟斤拷锟矫伙拷懈锟街�
						int tempI=this.LayerCompare(i, j, argOL, argHL, argIL);
						if((Math.abs(tempI)>1)||(tempI==0)){//锟斤拷越锟姐级锟侥节碉拷锟斤拷锟斤拷锟斤拷,同一锟斤拷诘锟街拷锟斤拷锟斤拷锟斤拷锟�
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
	
	private int LayerCompare(int argI,int argJ,int[] argOL,int[] argHL,int[] argIL){//锟斤拷锟截节碉拷i锟酵节碉拷j锟斤拷锟节诧拷蔚牟锟街�
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
	
	
	private int NodeType(int argI,double[] argD,int argB[]){//argI 锟节碉拷锟脚ｏ拷argD argI锟斤拷锟节斤拷锟斤拷锟斤拷
		int minIndex=0;
		int maxIndex=0;
		
		int res=-1;
		
		for(int i=0;i<argB.length;i++){//锟斤拷锟饺硷拷锟斤拷欠锟絙ias锟节碉拷
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
			res=1;//锟斤拷锟斤拷诘锟�
		}else if(maxIndex-argI>0&&minIndex-argI<0){
			res=2;//锟斤拷锟截节碉拷
		}else if(maxIndex-argI<0&&minIndex-argI<0){
			res=3;//锟斤拷锟斤拷诘锟�
		}
		
	//	System.out.println("锟节碉拷锟斤拷:"+argI+" min锟斤拷"+minIndex+" max:"+maxIndex+" res:"+res);
		
		return res;
	}
	

}
