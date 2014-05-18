package com.lqyandpy.crf;

public interface ActivationFunction {
	public double evaluate(double argD);
	
	public double derivation(double argD);
}
