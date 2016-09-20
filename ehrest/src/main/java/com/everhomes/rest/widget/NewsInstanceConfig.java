package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class NewsInstanceConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private Long itemGroup;
    
    private Long categoryId;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(Long itemGroup) {
		this.itemGroup = itemGroup;
	}

}
