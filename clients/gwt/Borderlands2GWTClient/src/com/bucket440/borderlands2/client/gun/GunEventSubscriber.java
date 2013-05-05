package com.bucket440.borderlands2.client.gun;

public interface GunEventSubscriber {
	public void gunAdded(Gun gun);
	public void gunRemoved(Gun gun);
	public void gunSelected(Gun gun);
}
