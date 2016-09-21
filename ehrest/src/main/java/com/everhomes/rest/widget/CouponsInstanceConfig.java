package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class CouponsInstanceConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private String itemGroup;

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

}
