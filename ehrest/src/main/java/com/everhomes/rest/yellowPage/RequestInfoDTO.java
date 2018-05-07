package com.everhomes.rest.yellowPage;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id：申请id</li>
 *  <li>createTime：申请时间</li>
 *  <li>creatorName：用户姓名</li>
 *  <li>creatorMobile：手机号</li>
 *  <li>creatorOrganization：机构名称</li>
 *  <li>serviceOrganization：服务机构名</li>
 *  <li>templateType : 模板类型</li>
 *  <li>jumpType : 跳转类型 0：无， 1：普通模板，2：功能模块 参考{@link com.everhomes.rest.yellowPage.JumpType}</li>
 *  <li>appId: 所属应用id</li>
 *  <li>secondCategoryId: 服务联盟小类id</li>
 *  <li>secondCategoryName: 服务联盟小类名称</li>
 *  <li>workflowStatus: 工作流状态，{@link com.everhomes.rest.yellowPage.ServiceAllianceWorkFlowStatus}</li>
 * </ul>
 */
public class RequestInfoDTO {
	
	private Long id;
	
	private String creatorName;
	
	private String creatorMobile;
	
	private String creatorOrganization;
	
	private String serviceOrganization;
	
	private String createTime;

	private String templateType;
	
	private Long jumpType;
	
	private Long flowCaseId;

	private Long appId;

	private Long secondCategoryId;
	
	private String secondCategoryName;
	
	private Byte workflowStatus;

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

	public String getSecondCategoryName() {
		return secondCategoryName;
	}

	public void setSecondCategoryName(String secondCategoryName) {
		this.secondCategoryName = secondCategoryName;
	}

	public Byte getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(Byte workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatorOrganization() {
		return creatorOrganization;
	}

	public void setCreatorOrganization(String creatorOrganization) {
		this.creatorOrganization = creatorOrganization;
	}

	public String getServiceOrganization() {
		return serviceOrganization;
	}

	public void setServiceOrganization(String serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getCreatorMobile() {
		return creatorMobile;
	}

	public void setCreatorMobile(String creatorMobile) {
		this.creatorMobile = creatorMobile;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public Long getJumpType() {
		return jumpType;
	}

	public void setJumpType(Long jumpType) {
		this.jumpType = jumpType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
