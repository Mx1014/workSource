package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class ActionBarsInstanceConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private Long itemGroup;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Long getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(Long itemGroup) {
		this.itemGroup = itemGroup;
	}

}
