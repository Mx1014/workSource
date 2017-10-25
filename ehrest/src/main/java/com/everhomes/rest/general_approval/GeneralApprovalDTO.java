package com.everhomes.rest.general_approval;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>id : id</li>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId </li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations</li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 * <li>organizationId: 属于的公司 ID</li>
 * <li>supportType: APP可用，WEB 可用，APP 与 WEB 都可用 {@link com.everhomes.rest.general_approval.GeneralApprovalSupportType}</li>
 * <li>projectType : </li>
 * <li>projectId : </li>
 * <li>formOriginId: 原始 formId，如果修改了版本，则原始的数据保留</li>
 * <li>approvalName : 审批名称</li>
 * <li>status: 查询approval的状态 默认是包括禁用和启用的 1-禁用 2-启用{@link com.everhomes.rest.general_approval.GeneralApprovalStatus}</li>
 * <li>approvalAttribute: 审批属性 比如: DEFAULT-系统默认 参考{@link com.everhomes.rest.general_approval.GeneralApprovalAttribute}</li>
 * <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改</li>
 * <li>deleteFlag: 是否可修改 0-不可删除 1-可以删除</li>
 * <li>iconUri: 图标的uri</li>
 * <li>iconUrl: 图标的url</li>
 * </ul>
 * @author janson
 *
 */
public class GeneralApprovalDTO {
	private Long     id;
	private Integer     namespaceId;
    private String     ownerType;
    private Long     ownerId;
    private String     moduleType;
    private Long     moduleId;
    private String     projectType;
    private Long     projectId;
    private Long     organizationId;
    private Byte supportType;
    private Long     formOriginId;
    private Long     formVersion;
    private Byte     status;
    private String approvalName;
    private String formName;
    private String flowName;

    private Timestamp     createTime;
    private Timestamp     updateTime;

    //	added by R.
	private Byte modifyFlag;
	private Byte deleteFlag;
    private String approvalAttribute;
	private String iconUri;
	private String iconUrl;

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

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Byte getSupportType() {
		return supportType;
	}

	public void setSupportType(Byte supportType) {
		this.supportType = supportType;
	}

	public String getApprovalName() {
		return approvalName;
	}

	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
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

	public String getApprovalAttribute() {
		return approvalAttribute;
	}

	public void setApprovalAttribute(String approvalAttribute) {
		this.approvalAttribute = approvalAttribute;
	}

	public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}

