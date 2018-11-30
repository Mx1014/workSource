package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <p>参数:</p>
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>appId: 应用id</li>
 * <li>momentId: 动态id</li>
 * </ul>
 */
public class GetMomentDetailCommand {
    private Long organizationId;
    private Long appId;
    private Long momentId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getMomentId() {
        return momentId;
    }

    public void setMomentId(Long momentId) {
        this.momentId = momentId;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
