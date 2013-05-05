package com.bucket440.borderlands2.client.gun;

public enum GunType {
	
	PISTOL("Pistol"), ASSAULT_RIFLE("Assault Rifle"), SHOTGUN("Shotgun"), SNIPER_RIFLE("Sniper Rifle"), SUBMACHINE("Submachine"), ROCKET_LAUNCHER("Rocket Launcher");
	
	private String displayName;
	
	private GunType(String displayName){
		this.displayName = displayName;
	}
	
	public String getDisplayName(){
		return displayName;
	}
}
