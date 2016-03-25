package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>type: 黄页类型，{@link com.everhomes.rest.yellowPage.YellowPageType}</li>
 * <li>latitude: 请求人所在位置对应的纬度</li>
 * <li>conditionJson: 搜索条件</li>
 * <li>globalFlag: 是否全局搜索</li>
 * <li>pageOffset: 偏移量</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class GetYellowPageListCommand {

	private Long communityId;
	
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Byte type;
	
	private String keywords;

	private Long parentId;
	
	private Long pageAnchor;
    
	private Integer pageSize;

	private java.lang.String   serviceType;
	
	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

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

	public java.lang.String getServiceType() {
		return serviceType;
	}

	
	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public void setServiceType(java.lang.String serviceType) {
		this.serviceType = serviceType;
	}
}
