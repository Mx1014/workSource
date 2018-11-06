package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId</li>
 *     <li>ownerType: 对象类型，默认为 EhOrganizations {@link com.everhomes.entity.EntityType}</li>
 *     <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 *     <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 *     <li>organizationId: 属于的公司 ID</li>
 *     <li>formName: 表单名字</li>
 *     <li>projectId: projectId</li>
 *     <li>projectType: projectType</li>
 *     <li>formAttribute: formAttribute</li>
 *     <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改</li>
 *     <li>deleteFlag: 是否可删除 0-不可删除 1-可以删除</li>
 *     <li>formFields: 表单控件数据 {@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * </ul>
 */
public class CreateApprovalFormCommand {

	private Long ownerId;
	private String ownerType;
	private Long moduleId;
	private String moduleType;
	private Long organizationId;
	private String formName;

	private Long projectId;
	private String projectType;

	//added by R.
	private String formAttribute;
	private Byte modifyFlag;
	private Byte deleteFlag;

	@ItemType(GeneralFormFieldDTO.class)
	List<GeneralFormFieldDTO> formFields;

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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public List<GeneralFormFieldDTO> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<GeneralFormFieldDTO> formFields) {
		this.formFields = formFields;
	}

	public String getFormAttribute() {
		return formAttribute;
	}

	public void setFormAttribute(String formAttribute) {
		this.formAttribute = formAttribute;
	}

	public Byte getModifyFlag() {
		return modifyFlag;
	}

	public void setModifyFlag(Byte modifyFlag) {
		this.modifyFlag = modifyFlag;
	}

	public Byte getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
