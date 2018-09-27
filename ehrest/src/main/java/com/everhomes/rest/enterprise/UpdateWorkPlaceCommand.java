package com.everhomes.rest.enterprise;

import com.everhomes.rest.address.CreateOfficeSiteCommand;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>officeSites: 办公地点名称以及楼栋门牌集合</li>
 * </ul>
 */
public class UpdateWorkPlaceCommand {
    private Long organizationId;
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
