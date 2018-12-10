package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出工位预定空间的请求参数
  * <li>namespaceId : namespaceId </li>
  * <li>ownerType : community 工位发布的范围</li>
 * <li>ownerId : communityId 范围的id</li>
 * <li>beginDate: 查询开始时间</li>
 * <li>endDate: 查询结束时间</li>
 * <li>spaceName: 查询空间名</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 一页的大小</li> 
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * <li>orderStatus：订单状态{@link com.everhomes.rest.officecubicle.OfficeCubiceOrderStatus}</li>
 * <li>requestType:订单来源 {@link com.everhomes.rest.officecubicle.OfficeCubicleRequestType}</li>
 * <li>paidType:支付方式,10001-支付宝，10002-微信 {@link com.everhomes.rest.organization.VendorType}</li>
 * <li>paidMode:支付类型 {@link com.everhomes.rest.general.order.GorderPayType}</li>
 * <li>spaceId:空间ID</li>
 * <li>rentType:1长租，0短租</li>
 * </ul>
 */
public class SearchCubicleOrdersCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long beginTime;
	private Long endTime;
	private String spaceName;
	private Long spaceId;
	private String reserveKeyword;
	private Long pageAnchor;
    
	private Integer pageSize;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;
	private Byte orderStatus;
	private String paidType;
	private Byte paidMode;
	private Byte requestType;
	private Byte rentType;
	public Long getCurrentPMId() {
		return currentPMId;
	}

	
	public Long getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}


	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}
	
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
 

	public Integer getNamespaceId() {
		return namespaceId;
	}


	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}


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

	public Long getBeginTime() {
		return beginTime;
	}


	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}


	public Long getEndTime() {
		return endTime;
	}


	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}


	public String getSpaceName() {
		return spaceName;
	}


	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}


	public String getReserveKeyword() {
		return reserveKeyword;
	}


	public void setReserveKeyword(String reserveKeyword) {
		this.reserveKeyword = reserveKeyword;
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

	public Byte getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getPaidType() {
		return paidType;
	}


	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}


	public Byte getPaidMode() {
		return paidMode;
	}

	public void setPaidMode(Byte paidMode) {
		this.paidMode = paidMode;
	}

	public Byte getRequestType() {
		return requestType;
	}

	public void setRequestType(Byte requestType) {
		this.requestType = requestType;
	}

	public Byte getRentType() {
		return rentType;
	}

	public void setRentType(Byte rentType) {
		this.rentType = rentType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
