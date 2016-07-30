package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul>预订的actiondata
 * <li>resourceTypeId: 资源类型的id</li>
 * <li>pageType: 预定展示0代表默认页面DefaultType, 1代表定制页面CustomType</li>
 * <li>communityFilterFlag: 是否有园区筛选器 0-没有 1-有</li>
 * </ul>
 */
public class RentalActionData implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5152885247968357254L;

	private Long resourceTypeId;
	private Byte pageType;
	private Byte communityFilterFlag; 
	
	
    public Long getResourceTypeId() {
		return resourceTypeId;
	}



	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}



	public Byte getPageType() {
		return pageType;
	}



	public void setPageType(Byte pageType) {
		this.pageType = pageType;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public Byte getCommunityFilterFlag() {
		return communityFilterFlag;
	}



	public void setCommunityFilterFlag(Byte communityFilterFlag) {
		this.communityFilterFlag = communityFilterFlag;
	}
}
