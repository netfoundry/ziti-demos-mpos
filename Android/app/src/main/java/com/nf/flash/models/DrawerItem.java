package com.nf.flash.models;

public class DrawerItem {

	private String drawerItemName;
	private int drawerImgResID;
	
	public DrawerItem(String itemName, int imgResID) {
		super();
		drawerItemName = itemName;
		this.drawerImgResID = imgResID;
	}
	
	public String getItemName() {
		return drawerItemName;
	}

	public int getImgResID() {
		return drawerImgResID;
	}
	
}
