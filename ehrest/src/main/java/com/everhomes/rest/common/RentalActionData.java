package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul>预订的actiondata
 * <li>resourceTypeId: 资源类型的id</li>
 * <li>pageType: 预定展示0代表默认页面DefaultType, 1代表定制页面CustomType</li>
 * <li>communityFilterFlag: 是否有园区筛选器 0-没有 1-有</li>
 * <li>payMode: 支付模式 (工作流模式) {@link com.everhomes.rest.rentalv2.admin.PayMode}</li>
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
	private Byte payMode;
	
	
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



	public Byte getPayMode() {
		return payMode;
	}



	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}
}
