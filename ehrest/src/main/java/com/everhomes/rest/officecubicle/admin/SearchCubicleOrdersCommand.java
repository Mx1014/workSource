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
 * <li>reserveKeyword: 查询预订人关键字</li>
 * <li>workFlowStatus: 工作流状态， {@link com.everhomes.rest.officecubicle.OfficeOrderWorkFlowStatus}</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 一页的大小</li> 
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * <li>orderStatus</li>
 * </ul>
 */
public class SearchCubicleOrdersCommand {
	private Integer namespaceId;
	private String ownerType;
	private Long ownerId;
	private Long beginDate;
	private Long endDate;
	private String spaceName;
	private String reserveKeyword;
	private Byte workFlowStatus;
	private Long pageAnchor;
    
	private Integer pageSize;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;
	private Byte orderStatus;
	private Byte paidType;
	private Byte paidMode;
	private Byte requestType;
	private Byte rentType;
	public Long getCurrentPMId() {
		return currentPMId;
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


	public Byte getWorkFlowStatus() {
		return workFlowStatus;
	}


	public void setWorkFlowStatus(Byte workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}


	public Long getBeginDate() {
		return beginDate;
	}


	public void setBeginDate(Long beginDate) {
		this.beginDate = beginDate;
	}


	public Long getEndDate() {
		return endDate;
	}


	public void setEndDate(Long endDate) {
		this.endDate = endDate;
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

	public Byte getPaidType() {
		return paidType;
	}

	public void setPaidType(Byte paidType) {
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
