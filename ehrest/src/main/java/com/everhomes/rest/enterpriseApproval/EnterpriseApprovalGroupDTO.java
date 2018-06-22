package com.everhomes.rest.enterpriseApproval;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>id: 组id</li>
 * <li>ownerType: 对象类型 EhOrganizations</li>
 * <li>ownerId: 属于的对象 ID，如果所属类型是 EhOrganizations，则 ownerId 等于 organizationId</li>
 * <li>name: 组名称</li>
 * <li>approvals: 审批列表 参考{@link com.everhomes.rest.enterpriseApproval.EnterpriseApprovalDTO}</li>
 * <li>groupAttribute: 组属性 DEFAULT-系统默认 CUSTOMIZE</li>
 * </ul>
 */
public class EnterpriseApprovalGroupDTO {

    private Long id;

    private String ownerType;

    private Long ownerId;

    private String name;

    @ItemType(EnterpriseApprovalDTO.class)
    private List<EnterpriseApprovalDTO> approvals;

    private String groupAttribute;

    public EnterpriseApprovalGroupDTO() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EnterpriseApprovalDTO> getApprovals() {
        return approvals;
    }

    public void setApprovals(List<EnterpriseApprovalDTO> approvals) {
        this.approvals = approvals;
    }

    public String getGroupAttribute() {
        return groupAttribute;
    }

    public void setGroupAttribute(String groupAttribute) {
        this.groupAttribute = groupAttribute;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
