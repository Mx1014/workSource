package com.everhomes.rest.officecubicle;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定空间的请求参数
 * <li>cityId: 城市id</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 一页的大小</li> 
 * </ul>
 */
public class QuerySpacesCommand { 
	private Long cityId;
	
	private Long pageAnchor;
    
	private Integer pageSize;
	
 
	public Long getPageAnchor() {
		return pageAnchor;
	}


	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public Long getCityId() {
		return cityId;
	}


	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
}
