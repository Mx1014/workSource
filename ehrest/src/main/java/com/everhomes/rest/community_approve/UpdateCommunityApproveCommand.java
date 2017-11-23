package com.everhomes.rest.community_approve;

/**
 * <ul>
 * <li>approveName:审批名称</li>
 * <li>status:启用状态 0:不启用 1:启用</li>
 * <li>formOriginId:表单id</li>
 * </ul>
 *
 */
public class UpdateCommunityApproveCommand {

    private Long id;
    private String approveName;
    private Byte status;
    private Long formOriginId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }


}
