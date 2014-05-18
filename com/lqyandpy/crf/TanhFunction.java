package com.lqyandpy.crf;

public class TanhFunction implements ActivationFunction {

	@Override
	public double evaluate(double argD) {
		// TODO Auto-generated method stub
		
		if(argD<-10){
			return -1;
		}else if(argD>10){
			return 1;
		}
		
		double tempEX=Math.pow(Math.E, argD);
		double tempE_X=Math.pow(Math.E,(-1d)*argD);
		return (tempEX-tempE_X)/(tempEX+tempE_X);
		
	}

	@Override
	public double derivation(double argD) {
		// TODO Auto-generated method stub
		return 1-Math.pow(this.evaluate(argD), 2);
	}

}
