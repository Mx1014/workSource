package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>id : id</li>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId </li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations</li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 * <li>organizationId: 属于的公司 ID</li>
 * <li>supportType: APP可用，WEB 可用，APP 与 WEB 都可用 {@link com.everhomes.rest.general_approval.GeneralApprovalSupportType}</li>
 * <li>approvalName : 审批名称</li>
 * <li>approvalRemark : 审批备注</li>
 * <li>status: 查询approval的状态 默认是包括禁用和启用的 1-禁用 2-启用{@link com.everhomes.rest.general_approval.GeneralApprovalStatus}</li>
 * <li>scopes : 可见范围 参考{@link com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO}</li>
 * <li>approvalAttribute: 审批属性 比如: DEFAULT-系统默认 参考{@link com.everhomes.rest.general_approval.GeneralApprovalAttribute}</li>
 * <li>modifyFlag: 是否可修改 0-不可修改 1-可以修改</li>
 * <li>deleteFlag: 是否可修改 0-不可删除 1-可以删除</li>
 * <li>iconUri: 图标的uri</li>
 * <li>iconUrl: 图标的url</li>
 * <li>operatorUid: 更新人id</li>
 * <li>operatorName: 更新人名称</li>
 * </ul>
 * @author ryan
 *
 */
public class EnterpriseApprovalDTO {

    private Long id;
    private Long ownerId;
    private String ownerType;
    private String moduleType;
    private Long moduleId;
    private Long organizationId;
    private Byte supportType;
    private Byte status;
    private String approvalName;

    @ItemType(GeneralApprovalScopeMapDTO.class)
    private List<GeneralApprovalScopeMapDTO> scopes;
    private String approvalRemark;
    private Byte modifyFlag;
    private Byte deleteFlag;
    private String approvalAttribute;
    private Long approvalGroupId;
    private String iconUri;
    private String iconUrl;
    private Long operatorUid;
    private String operatorName;
    private Timestamp createTime;
    private Timestamp updateTime;

    public EnterpriseApprovalDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getSupportType() {
        return supportType;
    }

    public void setSupportType(Byte supportType) {
        this.supportType = supportType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public List<GeneralApprovalScopeMapDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<GeneralApprovalScopeMapDTO> scopes) {
        this.scopes = scopes;
    }

    public String getApprovalRemark() {
        return approvalRemark;
    }

    public void setApprovalRemark(String approvalRemark) {
        this.approvalRemark = approvalRemark;
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

    public Long getApprovalGroupId() {
        return approvalGroupId;
    }

    public void setApprovalGroupId(Long approvalGroupId) {
        this.approvalGroupId = approvalGroupId;
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

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
