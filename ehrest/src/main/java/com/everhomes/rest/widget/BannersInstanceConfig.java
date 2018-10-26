package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *
 * </ul>
 */
public class BannersInstanceConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private String itemGroup;

	private Long categoryId;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

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
