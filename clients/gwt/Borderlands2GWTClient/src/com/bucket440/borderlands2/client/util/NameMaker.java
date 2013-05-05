package com.bucket440.borderlands2.client.util;

public class NameMaker {
	private static NameMaker me;
	private int currentNumber = 0;
	
	private NameMaker(){}
	
	public static NameMaker nameMaker(){
		if(me == null){
			me = new NameMaker();
		}
		return me;
	}
	
	public String makeMeAName(){
		return "Some Gun #" + ++currentNumber;
	}
}
