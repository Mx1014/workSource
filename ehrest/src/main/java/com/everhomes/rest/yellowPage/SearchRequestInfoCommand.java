package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerType: 拥有者类型：EhOrganizations EhCommunities</li>
 *  <li>ownerId: 拥有者ID</li>
 *  <li>categoryId: 服务联盟大类id</li>
 *  <li>secondCategoryId: 服务联盟小类id</li>
 *  <li>workflowStatus: 工作流状态，{@link com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus}</li>
 *  <li>startDay: 开始时间</li>
 *  <li>endDay：结束时间</li>
 *  <li>keyword：关键字（创建请求的用户姓名和机构名称）</li>
 *  <li>templateType：申请类型 ServiceAlliance/Settle</li>
 *  <li>approvalType: 审批类型</li>
 *  <li>approvalStatus: 审批状态</li>
 *  <li>approvalNumber: 审批编号</li>
 *  <li>proposer: 申请人</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * </ul>
 */
public class SearchRequestInfoCommand {

	@NotNull
	private String   ownerType;
	@NotNull
	private Long     ownerId;
	
	private Long categoryId;
	
	private Long secondCategoryId;
	
	private Byte workflowStatus;
	
	private Long startDay;
	
	private Long endDay;
	
	private String keyword;
	
	private String templateType;
	
	private Long pageAnchor;
	
	private Integer pageSize;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;

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
	
	public Long getSecondCategoryId() {
		return secondCategoryId;
	}

	public void setSecondCategoryId(Long secondCategoryId) {
		this.secondCategoryId = secondCategoryId;
	}

	public Byte getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(Byte workflowStatus) {
		this.workflowStatus = workflowStatus;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getStartDay() {
		return startDay;
	}

	public void setStartDay(Long startDay) {
		this.startDay = startDay;
	}

	public Long getEndDay() {
		return endDay;
	}

	public void setEndDay(Long endDay) {
		this.endDay = endDay;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
