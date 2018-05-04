package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>name: 企业名称</li>
 *     <li>organizationId: 企业编号</li>
 *     <li>siteName: 办公地点名称</li>
 * </ul>
 */
public class EnterprisePropertyDTO {
    //企业名称
    private String name;
    //企业编号
    private Long organizationId;
    //办公地点名称
    private String siteName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
