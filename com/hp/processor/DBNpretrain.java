package com.hp.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import com.lqyandpy.DBN.DBNTrain;
import com.lqyandpy.DBN.SimpleDBN;
import com.lqyandpy.RBM.Data;
import com.lqyandpy.RBM.WeightDecay;


public class DBNpretrain extends Configured {
	
	public static SimpleDBN dbn;
	public static DBN_pretrain_params dbn_pretrain_params;
	
	public static class DBN_pretrain_params
	{
		public int[] hidden_nums;
		public double learning_rate;
		public double stop_threshold;
		public WeightDecay wd_func;
		public int max_try;
		public int batch_size;
		public int subset_size;
	}
	
	public static ArrayList<ArrayList<Double> > data = new ArrayList<ArrayList<Double> >();
	
	public static class MapClass extends Mapper<LongWritable,Text,Text,Text>
	{
		public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException
		{
			String[] data_s = value.toString().split(",");
			ArrayList<Double> sample = new ArrayList<Double>(data_s.length-1);
			for(int i = 0;i < sample.size();++i)
				sample.set(i, Double.parseDouble(data_s[i]));
			DBNpretrain.data.add(sample);
			if(DBNpretrain.data.size() >= DBNpretrain.dbn_pretrain_params.subset_size)
			{
				double[][] data_array = new double[DBNpretrain.data.size()][data_s.length-1];
				for(int i = 0;i < DBNpretrain.data.size();++i)
					for(int j = 0;j < data_s.length-1;++j)
						data_array[i][j] = DBNpretrain.data.get(i).get(j);
				Data tempD = new Data(data_array,false,true);
				
				DBNTrain train = new DBNTrain(tempD,DBNpretrain.dbn);
			}
			
			
		}
	}
}
