package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO;

import java.util.List;

/**
 * <ul>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId </li>
 * <li>ownerType: 对象类型，默认为 EhOrganizations </li>
 * <li>moduleId: 模块id - 每一个功能模块有自己的id</li>
 * <li>moduleType: 模块类型 默认"any-module" {@link com.everhomes.rest.flow.FlowModuleType}</li>
 * <li>organizationId: 属于的公司 ID</li>
 * <li>supportType: APP可用，WEB 可用，APP 与 WEB 都可用 {@link com.everhomes.rest.general_approval.GeneralApprovalSupportType}</li>
 * <li>approvalName: 审批名称</li>
 * <li>approvalGroupId: 审批所属组</li>
 * <li>approvalRemark : 审批备注</li>
 * <li>scopes : 可见范围 参考{@link com.everhomes.rest.general_approval.GeneralApprovalScopeMapDTO}</li>
 * </ul>
 * @author ryan
 *
 */
public class CreateEnterpriseApprovalCommand {

    private Long ownerId;
    private String ownerType;
    private String moduleType;
    private Long organizationId;
    private Long moduleId;
    private Byte supportType;
    private	String approvalName;
    private Long approvalGroupId;
    private String approvalRemark;

    @ItemType(GeneralApprovalScopeMapDTO.class)
    private List<GeneralApprovalScopeMapDTO> scopes;

    public CreateEnterpriseApprovalCommand() {
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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    public Long getApprovalGroupId() {
        return approvalGroupId;
    }

    public void setApprovalGroupId(Long approvalGroupId) {
        this.approvalGroupId = approvalGroupId;
    }

    public String getApprovalRemark() {
        return approvalRemark;
    }

    public void setApprovalRemark(String approvalRemark) {
        this.approvalRemark = approvalRemark;
    }

    public List<GeneralApprovalScopeMapDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<GeneralApprovalScopeMapDTO> scopes) {
        this.scopes = scopes;
    }
}
