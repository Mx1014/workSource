package com.everhomes.rest.general_approval;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id: id</li>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId </li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations {@link com.everhomes.entity.EntityType}</li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 * <li>organizationId: 属于的公司 ID</li>
 * <li>formOriginId: 原始 formId，如果修改了版本，则原始的数据保留</li>
 * <li>formVersion: 当前版本信息</li>
 * <li>templateType: 模板数据类型。未来表单可能同样的控件有不同的表达方式，则可以用这个类型区分 {@link GeneralFormTemplateType}</li>
 * <li>formName: 表单名字</li>
 * <li>formFields: 表单控件数据 {@link com.everhomes.rest.general_approval.GeneralFormFieldDTO}</li>
 * <li>formAttribute: 表单属性 比如: DEFAULT-系统默认 参考{@link com.everhomes.rest.general_approval.GeneralApprovalAttribute}</li>
 * <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改</li>
 * <li>deleteFlag: 是否可修改 0-不可删除 1-可以删除</li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormDTO {
    private Long     id;
    private Long     ownerId;
    private String     ownerType;
    private Long     moduleId;
    private String     moduleType;
    private Long     organizationId;
    private Long     formOriginId;
    private Long     formVersion;
    private Timestamp     updateTime;
    private Integer     namespaceId;
    private String     formName;
    private Byte     status;
    private String     templateType;
    private String     templateText;
    private Timestamp     createTime;

    //added by R
	private String formAttribute;
	private Byte modifyFlag;
	private Byte deleteFlag;

	private String operatorName;

    @ItemType(GeneralFormFieldDTO.class)
    List<GeneralFormFieldDTO> formFields;

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }public Long getOwnerId() {
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

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
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

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

