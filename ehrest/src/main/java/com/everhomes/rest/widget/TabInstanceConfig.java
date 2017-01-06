package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class TabInstanceConfig implements Serializable  {


    private static final long serialVersionUID = -9175649396549275602L;

	private String itemGroup;
    
    private Byte iconFlag;
    
    private String itemLocation;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

	public Byte getIconFlag() {
		return iconFlag;
	}

	public void setIconFlag(Byte iconFlag) {
		this.iconFlag = iconFlag;
	}

	public String getItemLocation() {
		return itemLocation;
	}

	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
	}

}
