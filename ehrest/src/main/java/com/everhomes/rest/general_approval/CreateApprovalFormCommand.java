package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId </li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations {@link com.everhomes.entity.EntityType}</li>
 * <li>organizationId: 属于的公司 ID</li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 * <li>formName: 表单名字</li>
 * <li>formFields: 表单控件数据 {@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * <li>formGroups: 表单控件组 {@link com.everhomes.rest.general_approval.GeneralFormGroupDTO}</li>
 * <li>approvalAttribute: 表单属性 比如: DEFAULT-系统默认 参考{@link com.everhomes.rest.general_approval.GeneralApprovalAttribute}</li>
 * <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改</li>
 * <li>deleteFlag: 是否可修改 0-不可删除 1-可以删除</li>
 * </ul>
 * @author janson
 *
 */
public class CreateApprovalFormCommand {
    private Long     ownerId;
    private String     ownerType;
    private Long     moduleId;
    private String     moduleType;
    private Long     organizationId;
    private String     formName;

	//added by R.
	private String formAttribute;
	private Byte modifyFlag;
	private Byte deleteFlag;

    @ItemType(GeneralFormFieldDTO.class)
    List<GeneralFormFieldDTO> formFields;

    @ItemType(GeneralFormGroupDTO.class)
    List<GeneralFormGroupDTO> formGroups;

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

	public List<GeneralFormGroupDTO> getFormGroups() {
		return formGroups;
	}

	public void setFormGroups(List<GeneralFormGroupDTO> formGroups) {
		this.formGroups = formGroups;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
