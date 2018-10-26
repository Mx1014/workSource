package com.everhomes.rest.widget;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

public class NewsInstanceConfig implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private String itemGroup;
    
    private Long categoryId;
    
    private String timeWidgetStyle ;

	private Long moduleId;

	private Long appId;

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

	public String getItemGroup() {
		return itemGroup;
	}

	public void setItemGroup(String itemGroup) {
		this.itemGroup = itemGroup;
	}

	public String getTimeWidgetStyle() {
		return timeWidgetStyle;
	}

	public void setTimeWidgetStyle(String timeWidgetStyle) {
		this.timeWidgetStyle = timeWidgetStyle;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
}
