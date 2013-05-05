package com.bucket440.borderlands2.client.gun;


public class Gun {

	public String name;
	public GunType type;
	public Double damage = 0D;
	public Integer damageMultiplier = 0;
	public Double accuracy = 100D;
	public Double fireRate;
	public Double reloadTime = 0D;
	public Integer magazineSize = 0;
	public Integer roundsPerShot = 1;
	public Double elementalDPS;
	public Double elementalChance;
	public CalculationResults calculationResults;
	
	public Gun(String name, 
			   GunType type, 
			   Double damage, 
			   Integer damageMultiplier, 
			   Double accuracy, 
			   Double fireRate, 
			   Double reloadTime, 
			   Integer magazineSize, 
			   Integer roundsPerShot, 
			   Double elementalDPS, 
			   Double elementalChance){
		this.name = name;
		this.type = type;
		this.damage = damage;
		this.damageMultiplier = damageMultiplier;
		this.accuracy = accuracy;
		this.fireRate = fireRate;
		this.reloadTime = reloadTime;
		this.magazineSize = magazineSize;
		this.roundsPerShot = roundsPerShot;
		this.elementalDPS = elementalDPS;
		this.elementalChance = elementalChance;
	}
	
	public boolean hasResults(){
		return calculationResults != null;
	}
}
