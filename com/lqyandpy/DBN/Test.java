package com.lqyandpy.DBN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.lqyandpy.RBM.*;
import com.lqyandpy.crf.*;
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		double[][] tempI=new double[][]{
//				  {0,0,0,0,0,1,1},
//				  {1,0,0,1,0,0,1},
//				  {1,0,1,1,0,1,1},
//				  {0,1,1,1,0,1,0},
//				  {1,1,0,1,0,1,1},
//				  {1,1,0,1,1,1,1},
//				  {1,0,1,0,0,1,0},
//				  {1,1,1,1,1,1,1},
//				  {1,1,1,1,0,1,1}
//				  };

		
//		List<ArrayList<Double>> tempI_ex = read_datafile("D:/eclipse/workspace32/CRF/bin/com/lqyandpy/DBN/test.txt"); 
//		if(tempI_ex == null)
//		{
//			System.out.print("not find data!\n");
//			return;
//		}
//		double[][] tempI =  new double[tempI_ex.size()][tempI_ex.get(0).size()-1];
//		for(int i = 0;i < tempI_ex.size();++i)
//		{
//			for(int j = 0;j < tempI_ex.get(i).size()-1;++j)
//				tempI[i][j] = tempI_ex.get(i).get(j);
//		}
//		System.out.print("data ready.\n");
//		tempI_ex = null;
			
		double[][] tempI = get_data_from_file("/mnt/hgfs/share/mnist_data_for_java/test.txt");
		Data tempD=new Data(tempI,false,true);//�����һ�������ĳһ���Խ��еĹ�һ������ͼ����Բ�����
		
		//Tool.PrintW(Data.Normalization(tempI));
	    
		//PermanentRBM tempP=new PermanentRBM();
		//tempP.ReadFromFile("D:\\���̱���\\RBMS\\rbm5.txt");
		//RBM tempR=tempP.ReBuildRBM();
		SimpleDBN tempS=new SimpleDBN();
		//tempS.Layers=2;
		DBNTrain.print_time("try to constructDBN");
		tempS.constructDBN(28*28,new int[]{500,500}, true);
		//RBM tempR = new RBM(7,20,false);
		
		//tempS.RebuildDBN("D:\\���̱���\\dbn.dat");
		
		
		
		//tempS.InsertRBM(tempR);
		//tempS.InsertRBM(tempR.CopyTiedRBM());
		//Tool.PrintW(tempS.getRBM(1).W);
		
		DBNTrain tempT=new DBNTrain(tempD,tempS);
		DBNTrain.print_time("begin to pretrain");
		//tempT.greedyLayerwiseTraining(0.1,0.001,new L1(),0,10);
		
		
//		byte[] s = tempS.toBytes();
//		SimpleDBN rebuiled = new SimpleDBN();
//		rebuiled.RebuildDBNbyBytes(s);
		
		System.out.print("prepare the ann weight\n");
		double[][] w_for_ann = tempS.get_ann_wight(10); 
		ANN ann = new ANN();
		ann.InitAnn(w_for_ann,tempS.ann_bias,new TanhFunction(),new TanhFunction());
		
		Trainer tempTR=new Trainer();
		tempTR.setLearningRate(0.2);
		System.out.print("train ann network");
		tempTR.Train(ann,tempI, 0.01,50);

	}
	
	public static List<ArrayList<Double> > read_datafile(String src)
	{
		try {
		File file = new File(src);
		if(!file.exists() || file.isDirectory())
			return null;
		List<ArrayList<Double> > ret = new ArrayList<ArrayList<Double>>();
		
		FileInputStream input = new FileInputStream(file);
		InputStreamReader inReader = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(inReader);
		String line;
		while((line = reader.readLine()) != null)
		{
			String[] line_data = line.split("\t");
			ArrayList<Double> cont = new ArrayList<Double>();
			for(String s:line_data)
				cont.add(Double.parseDouble(s));
			ret.add(cont);
			//System.out.print(ret.size()+"\n");
		}
		return ret;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static double[][] get_data_from_file(String filename)
	{
		File f = new File(filename);
		try {
			FileInputStream fin = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			String s;
			ArrayList<double[]> ret = new ArrayList<double[]>();
			
			while((s = br.readLine()) != null)
			{
				String[] caseline = s.split("\t");
				double[] x = new double[caseline.length];
				for(int i = 0;i < x.length;++i)
					x[i] = Double.parseDouble(caseline[i]);
				ret.add(x);
			}
			double[][] tmp = new double[ret.size()][ret.get(0).length];
			for(int i = 0;i < ret.size();++i)
				for(int j = 0;j < ret.get(0).length;++j)
					tmp[i][j] = ret.get(i)[j];
			return tmp;
		} catch (FileNotFoundException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
