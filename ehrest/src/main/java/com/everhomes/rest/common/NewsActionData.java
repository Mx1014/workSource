package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>categoryId: 新闻类型的id</li> 
 * </ul>
 */
public class NewsActionData implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

    private Long categoryId; 
    
    private String timeWidgetStyle ;
	 

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


	public String getTimeWidgetStyle() {
		return timeWidgetStyle;
	}


	public void setTimeWidgetStyle(String timeWidgetStyle) {
		this.timeWidgetStyle = timeWidgetStyle;
	}
 
}
