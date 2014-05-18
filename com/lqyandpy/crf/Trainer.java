package com.lqyandpy.crf;
import java.util.*;

public class Trainer {
	private double learningrate=0.15;
	
	public double getLearningRate(){
		return this.learningrate;
	}
	
	public void setLearningRate(double argD){
		this.learningrate=argD;
	}
	
	
	public double getWeight(Node argF,Node argT){
		double tempR=Double.NaN;
		for(Link l:argT.getLinks()){
			if(l.From.equals(argF)){
				tempR=l.Weight;
				break;
			}
		}
		return new Double(tempR).equals(Double.NaN)?0:tempR;
	}

	
	public double getMSE(double[][] argA,double[][] argT){
		double tempR=0;
		for(int i=0;i<argA.length;i++){
			for(int j=0;j<argA[0].length;j++){
				tempR+=Math.pow(argA[i][j]-argT[i][j],2);
			}
		}
		
		tempR=Math.sqrt(tempR/(double)(argA.length*argA[0].length));
		
		return tempR;
	}
	
	public void Train(ANN argA,double[][] argD,double argSC){
		ArrayList<OutputNode> tempON=argA.getOutputNodes();//输出层节点
		ArrayList<InputNode>  tempIN=argA.getInputNodes();//输入层节点
		ArrayList<HiddenNode> tempHN=argA.getHiddenNodes();//隐含层节点
		
		double[][] tempIM=new double[argD.length][tempIN.size()];//输入值Matrix
		double[][] tempOM=new double[argD.length][tempON.size()];//输出值Matrix
		
		for(int i=0;i<argD.length;i++){
			for(int j=0;j<argD[0].length;j++){
				if(j<tempIN.size()){
					tempIM[i][j]=argD[i][j];
				}else{
					tempOM[i][j-tempIN.size()]=argD[i][j];
				}
			}
		}
		
		int epoch=0;
		while(true){
			double[][] tempO=new double[argD.length][tempON.size()];
			for(int i=0;i<tempO.length;i++){
				double[] tempISO=argA.getOutput(tempIM[i]);
				for(int j=0;j<tempO[0].length;j++){
					tempO[i][j]=tempISO[j];
					System.out.println("["+tempO[i][j]+"  "+tempOM[i][j]+"]");
				}
			
			}
			
			double tempMSE=this.getMSE(tempO, tempOM);
			
			System.out.println("第"+epoch+"次训练，误差："+tempMSE+"\r\n");
			
			
			if(!new Double(tempMSE).equals(Double.NaN)&&tempMSE<=argSC){
				System.out.println("第"+epoch+"次训练网络收敛");
				break;
			}
			
			for(int i=0;i<tempIM.length;i++){
				this.OnePointTrain(argA, tempIM[i], tempOM[i], argSC);
			}

			epoch++;
		}
		
	}
	
	public void OnePointTrain(ANN argA,double[] argD,double[] argO,double argSC){//argD
			double[] tempO=argA.getOutput(argD);
			ArrayList<OutputNode> tempON=argA.getOutputNodes();
			int i=0;
			for(OutputNode on:tempON){//计算输出节点的delta
				on.setdelta(on.getActivateFunction().derivation(on.getCachedAccumulate())*(argO[i]-tempO[i]));
				i++;
			}
			
			ArrayList<HiddenNode> tempHN=argA.getHiddenNodes();
			for(HiddenNode hn:tempHN){//计算隐层节点的delta
				ArrayList<Node> tempTN=argA.getNodesFrom(hn);//隐层节点所连接的所有上层节点
				double tempE=0;
				for(Node tn:tempTN){
					ArrayList<Link> tempL=tn.getLinks();
					tempE+=tn.getdelta()*this.getWeight(hn, tn);//delta*weight
					
				}
				tempE*=hn.getActivateFunction().derivation(hn.getCachedAccumulate());
				hn.setdelta(tempE);
				}
		
			for(Node n:argA.Nodes){
				for(Link l:n.getLinks()){
					l.Weight+=this.learningrate*n.getdelta()*l.From.getActivateFunction().evaluate(l.From.getCachedAccumulate());
				}
			}
			
		}
	
	

}
