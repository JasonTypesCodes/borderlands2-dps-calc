package com.bucket440.borderlands2.client.gun;

public class CalculationResults {

	public Double bottomLine;
	public Double baseDPS;
	public Double withReload;
	public Double withElemental;
	public Double withAccuracy;
	public Double withReloadAndAccuracy;
	public Double withElementalAndAccuracy;
	public Double withReloadAndElemental;
	public Double withReloadAndElementalAndAccuracy;
	
	public Double getDPS(){
		return bottomLine;
	}
	
	public Double getDPS(boolean includeReload, boolean includeAccuracy, boolean includeElemental){
		if(!includeReload && !includeAccuracy && !includeElemental){
			return baseDPS;
		} else if(includeReload && !includeAccuracy && !includeElemental){
			return withReload;
		} else if(includeReload && includeAccuracy && !includeElemental){
			return withReloadAndAccuracy;
		} else if(includeReload && !includeAccuracy && includeElemental){
			return withReloadAndElemental;
		} else if(includeReload && includeAccuracy && includeElemental){
			return withReloadAndElementalAndAccuracy;
		} else if(!includeReload && includeAccuracy && !includeElemental){
			return withAccuracy;
		} else if(!includeReload && includeAccuracy && includeElemental){
			return withElementalAndAccuracy;
		} else if(!includeReload && !includeAccuracy && includeElemental){
			return withElemental;
		} else {
			return bottomLine;
		}
	}
	
}
