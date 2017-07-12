package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class BulletinsInstanceConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private String itemGroup;

	private Integer rowCount;

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

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
}
