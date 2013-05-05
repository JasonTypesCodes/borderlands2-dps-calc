package com.bucket440.borderlands2.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bucket440.borderlands2.client.gun.Gun;
import com.bucket440.borderlands2.client.gun.GunEventManager;
import com.bucket440.borderlands2.client.gun.GunEventSubscriber;
import com.bucket440.borderlands2.client.gun.GunType;
import com.google.gwt.user.client.ui.TabPanel;

public class GunResultDisplay extends TabPanel implements GunEventSubscriber{
	
	Map<GunType, GunTable> tableMap = new HashMap<GunType, GunTable>();
	List<GunType> tabOrder = new ArrayList<GunType>();
	
	public GunResultDisplay(){
		super();
		buildTabs();
		selectTab(GunType.PISTOL);
		GunEventManager.getManagerInstance().addSubscriber(this);
	}
	
	public void selectTab(GunType type){
		int position = tabOrder.indexOf(type);
		selectTab(position);
	}
	
	public void addTab(GunTable gunTable, GunType gunType){
		tabOrder.add(gunType);
		tableMap.put(gunType, gunTable);
		add(gunTable, gunType.getDisplayName());
	}
	
	private void buildTabs(){
		for(GunType gunType : GunType.values()){
			addTab(new GunTable(), gunType);
		}
	}

	@Override
	public void gunAdded(Gun gun) {
		if(gun.type != null){
			GunTable gunTable = tableMap.get(gun.type);
			selectTab(gun.type);
			gunTable.addGun(gun);
		}
	}

	@Override
	public void gunRemoved(Gun gun) {
		if(gun.type != null){
			GunTable gunTable = tableMap.get(gun.type);
			selectTab(gun.type);
			gunTable.removeGun(gun);
		}
	}

	@Override
	public void gunSelected(Gun gun) {}
}
