package com.bucket440.borderlands2.client.gun;

import com.bucket440.borderlands2.client.util.NameMaker;
import com.google.gwt.user.client.Random;

public class RandomGunFactory {
	
	public static Gun randomGun(){
		GunType type = GunType.values()[Random.nextInt(GunType.values().length)];
		return randomGun(type);
	}
	
	public static Gun randomGun(GunType type){
		Gun result = new Gun(NameMaker.nameMaker().makeMeAName(),type,Random.nextDouble(),Random.nextInt(),Random.nextDouble(),Random.nextDouble(),Random.nextDouble(),Random.nextInt(),Random.nextInt(),Random.nextDouble(),Random.nextDouble());
		
		result.calculationResults = randomResults();
		
		return result;
	}
	
	private static CalculationResults randomResults(){
		CalculationResults result = new CalculationResults();
		result.baseDPS = Random.nextDouble();
		result.bottomLine = Random.nextDouble();
		result.withAccuracy = Random.nextDouble();
		result.withElemental = Random.nextDouble();
		result.withElementalAndAccuracy = Random.nextDouble();
		result.withReload = Random.nextDouble();
		result.withReloadAndAccuracy = Random.nextDouble();
		result.withReloadAndElemental = Random.nextDouble();
		result.withReloadAndElementalAndAccuracy = Random.nextDouble();
		return result;
	}
}
