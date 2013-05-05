package com.bucket440.borderlands2.client.widget;

import com.bucket440.borderlands2.client.gun.GunType;
import com.google.gwt.user.client.ui.ListBox;

public class GunTypeDropDown extends ListBox{

	public GunTypeDropDown(){
		super();
		for(GunType gunType : GunType.values()){
			addItem(gunType.getDisplayName(), gunType.name());
		}
		setVisibleItemCount(1);		
	}
	
	public GunType getSelectedType(){
		String name = getValue(getSelectedIndex());
		return GunType.valueOf(name);
	}
}
