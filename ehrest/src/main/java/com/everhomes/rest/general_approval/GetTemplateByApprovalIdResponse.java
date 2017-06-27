package com.everhomes.rest.general_approval;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>projectId: 项目 ID</li>
 * <li>projectType: 项目类型</li>
 * <li>formOriginId: 表单 ID</li>
 * <li>formVersion: 表单当前版本信息</li>
 * <li>formFields: 表单控件数据 {@link GeneralFormFieldDTO}</li>
 * <li>values: form表单对应的值信息 {@link com.everhomes.rest.general_approval.PostApprovalFormItem}</li>
 * </ul>
 * @author janson
 *
 */
public class GetTemplateByApprovalIdResponse {
	private Long approvalId;
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private Long organizationId;
	private Long moduleId;
	private String moduleType;
	private Long projectId;
	private String projectType;
	private Long formOriginId;
	private Long formVersion;
	private Byte status;
    private Long flowCaseId;
	private Timestamp updateTime;
	private Timestamp createTime;
	
	@ItemType(GeneralFormFieldDTO.class)
	List<GeneralFormFieldDTO> formFields;
	
	@ItemType(PostApprovalFormItem.class)
	List<PostApprovalFormItem> values;

	public Long getApprovalId() {
		return approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}

	public List<PostApprovalFormItem> getValues() {
		return values;
	}

	public void setValues(List<PostApprovalFormItem> values) {
		this.values = values;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Long getFormOriginId() {
		return formOriginId;
	}

	public void setFormOriginId(Long formOriginId) {
		this.formOriginId = formOriginId;
	}

	public Long getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(Long formVersion) {
		this.formVersion = formVersion;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<GeneralFormFieldDTO> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<GeneralFormFieldDTO> formFields) {
		this.formFields = formFields;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getFlowCaseId() {
		return flowCaseId;
	}

	public void setFlowCaseId(Long flowCaseId) {
		this.flowCaseId = flowCaseId;
	}
}
