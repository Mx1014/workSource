package com.everhomes.rest.servicehotline;

/**
 * Created by Administrator on 2017/12/27.
 */
public class GetUserInfoByIdCommand {
    private Long id;
    private Long orgId;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
