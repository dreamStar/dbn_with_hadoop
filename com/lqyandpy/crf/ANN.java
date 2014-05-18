package com.lqyandpy.crf;

import java.util.*;

public class ANN {
	public ArrayList<Node> Nodes=new ArrayList<Node>();//
	
	public int NetworkSize;
	
	public ArrayList<InputNode> getInputNodes(){//输入变量列表
		ArrayList<InputNode> tempN=new ArrayList<InputNode>();
		for(Node n:Nodes){
			if(n.getNodeType()==3){
				tempN.add((InputNode)n);
			}
		}
		Collections.sort(tempN);
		return tempN;
	}
	
	public ArrayList<OutputNode> getOutputNodes(){//输出单元列表
		ArrayList<OutputNode> tempN=new ArrayList<OutputNode>();
		for(Node n:Nodes){
			if(n.getNodeType()==1){
				tempN.add((OutputNode)n);
			}
		}
		Collections.sort(tempN);
		return tempN;
	}
	
	public ArrayList<HiddenNode> getHiddenNodes(){//隐含单元列表
		ArrayList<HiddenNode> tempN=new ArrayList<HiddenNode>();
		for(Node n:Nodes){
			if(n.getNodeType()==2){
				tempN.add((HiddenNode)n);
			}
		}
		Collections.sort(tempN);
		return tempN;
	}
	
	public double[] getOutput(double[] argD){//输入变量，求值
		ArrayList<OutputNode> tempONS=this.getOutputNodes();
		double[] tempR=new double[tempONS.size()];//准备输出
		
		ArrayList<InputNode> tempINS=this.getInputNodes();//
		
		//assert(argD.length==tempINS.size());
		for(int i=0;i<argD.length;i++){
			tempINS.get(i).Value=argD[i];
		}//为输入节点赋值
		
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
		case(1):break;//顶层节点，没有从它出发的连接
		case(2):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//隐层节点，有从它出发的连接
		case(3):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//输入层
		case(4):
			for(Node n:this.Nodes){
				ArrayList<Link> tempLK=n.getLinks();
				for(Link l:n.getLinks()){
					if(l.From.equals(argN)){
						tempL.add(n);
					}
				}
			}
			break;//偏移
		default:break;
		}
		
		return tempL;
	}
	
	public void InitAnn(double[][] argD,int[] argB,ActivationFunction argHF,ActivationFunction argOF){//传入一个方阵，网络结构和初始权值.从最高层开始编号，越往下编号越大
		//argB bias节点编号
		this.NetworkSize=argD[0].length;
		
		for(int i=0;i<this.NetworkSize;i++){
			switch(this.NodeType(i, argD[i],argB)){
			case(1)://新建顶层节点
				OutputNode tempON=new OutputNode();
				tempON.id=i;
				tempON.setActivateFunction(argOF);
				Nodes.add(tempON);
				System.out.println("新建顶层节点 "+tempON.id);
			break;
			case(2)://新建隐层节点
				HiddenNode tempHN=new HiddenNode();
				tempHN.id=i;
				tempHN.setActivateFunction(argHF);
				Nodes.add(tempHN);
				System.out.println("新建隐层节点 "+tempHN.id);
			break;
			case(3)://新建输入节点
				InputNode tempIN=new InputNode();
				tempIN.id=i;
				Nodes.add(tempIN);
				System.out.println("新建输入节点 "+tempIN.id);
			break;
			case(4)://新建bias节点
				BiasNode tempBN=new BiasNode();
				tempBN.id=i;
				Nodes.add(tempBN);
				System.out.println("新建偏移节点 "+tempBN.id);
			break;
			default:break;
			}
		}
		
		for(Node n:Nodes){
			int tempID=n.getID();
			double[] tempWV=argD[tempID];//与节点关联的权值向量
			
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
	
	
	
	public double[][] ConstructInitMatrix(int[][] argM){//通用初始化器
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
				if(i==j){//没有自联结
					tempR[i][j]=Double.NaN;
				}else{
					if(new Double(tempR[i][j]).equals(Double.POSITIVE_INFINITY)){//如果还没有赋值
						int tempI=this.LayerCompare(i, j, argM);
						if((Math.abs(tempI)>1)||(tempI==0)){//跨越层级的节点无连接,同一层节点之间无链接
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
	
	/*三层结构的专用初始化器*/
	public double[][] ConstructInitMatrix(int argI,int[] argOL,int[] argHL,int[] argIL){
		
		double[][] tempR=new double[argI][argI];
		
		for(int i=0;i<argI;i++)
			for(int j=0;j<argI;j++)
				tempR[i][j]=Double.POSITIVE_INFINITY;
		
		for(int i=0;i<argI;i++){
			for(int j=0;j<argI;j++){
				if(i==j){//没有自联结
					tempR[i][j]=Double.NaN;
				}else{
					if(new Double(tempR[i][j]).equals(Double.POSITIVE_INFINITY)){//如果还没有赋值
						int tempI=this.LayerCompare(i, j, argOL, argHL, argIL);
						if((Math.abs(tempI)>1)||(tempI==0)){//跨越层级的节点无连接,同一层节点之间无链接
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
	
	private int LayerCompare(int argI,int argJ,int[] argOL,int[] argHL,int[] argIL){//返回节点i和节点j所在层次的差值
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
	
	
	private int NodeType(int argI,double[] argD,int argB[]){//argI 节点编号，argD argI的邻接向量
		int minIndex=0;
		int maxIndex=0;
		
		int res=-1;
		
		for(int i=0;i<argB.length;i++){//首先检查是否bias节点
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
			res=1;//输出节点
		}else if(maxIndex-argI>0&&minIndex-argI<0){
			res=2;//隐藏节点
		}else if(maxIndex-argI<0&&minIndex-argI<0){
			res=3;//输入节点
		}
		
	//	System.out.println("节点标号:"+argI+" min："+minIndex+" max:"+maxIndex+" res:"+res);
		
		return res;
	}
	

}
