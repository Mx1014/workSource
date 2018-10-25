package com.everhomes.rest.enterprise;

import com.everhomes.rest.address.CreateOfficeSiteCommand;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 组织ID</li>
 *     <li>officeSites: 关联的楼栋门牌</li>
 *     <li>siteName: 项目办公地点名称</li>
 *     <li>communityId: 项目编号</li>
 * </ul>
 */
public class CreateSettledEnterpriseCommand {
    //组织ID
    private Long organizationId;

    //关联的楼栋门牌
    private List<CreateOfficeSiteCommand> officeSites;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<CreateOfficeSiteCommand> getOfficeSites() {
        return officeSites;
    }

    public void setOfficeSites(List<CreateOfficeSiteCommand> officeSites) {
        this.officeSites = officeSites;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
