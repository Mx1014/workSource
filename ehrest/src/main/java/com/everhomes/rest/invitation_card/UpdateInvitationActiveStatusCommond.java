package com.everhomes.rest.invitation_card;

import com.everhomes.rest.general_approval.GeneralApprovalStatus;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId:namespaceId</li>
 *     <li>id : 想要修改状态的审批ID</li>
 *     <li>status : 想要修改的状态</li>
 * </ul>
 */
public class UpdateInvitationActiveStatusCommond {
    private Integer namespaceId;
    private Long moduleId;
    private Long id;
    private Byte status;


    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
}
