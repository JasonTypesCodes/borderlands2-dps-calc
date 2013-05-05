package com.bucket440.borderlands2.client.gun;

import java.util.ArrayList;
import java.util.List;

public class GunEventManager {
	
	private static GunEventManager me;
	private List<GunEventSubscriber> subscribers;
	
	private GunEventManager(){ 
		subscribers = new ArrayList<GunEventSubscriber>();
	}
	
	public static GunEventManager getManagerInstance(){
		if(me == null){
			me = new GunEventManager();
		}
		return me;
	}
	
	public void addSubscriber(GunEventSubscriber subscriber){
		subscribers.add(subscriber);
	}
	
	public void publishGunAddedEvent(Gun gun){
		if(gun != null){
			for(GunEventSubscriber subscriber : subscribers){
				subscriber.gunAdded(gun);
			}			
		}
	}
	
	public void publishGunRemovedEvent(Gun gun){
		if(gun != null){
			for(GunEventSubscriber subscriber : subscribers){
				subscriber.gunRemoved(gun);
			}			
		}
	}
	
	public void publishGunSelectedEvent(Gun gun){
		if(gun != null){
			for(GunEventSubscriber subscriber : subscribers){
				subscriber.gunSelected(gun);
			}			
		}
	}
}
