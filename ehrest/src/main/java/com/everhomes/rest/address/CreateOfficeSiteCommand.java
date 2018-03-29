package com.everhomes.rest.address;

import com.everhomes.rest.organization.OrganizationSiteApartmentDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>
 * <li>siteName: 办公地点名称</li>
 * <li>communityId: 所在项目</li>
 * <li>siteDtos: 关联的楼栋门牌，参考{@link OrganizationSiteApartmentDTO}</li>
 * </ul>
 */
public class CreateOfficeSiteCommand {
    private String siteName;
    private Long communityId;
    private List<OrganizationSiteApartmentDTO> siteDtos;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<OrganizationSiteApartmentDTO> getSiteDtos() {
        return siteDtos;
    }

    public void setSiteDtos(List<OrganizationSiteApartmentDTO> siteDtos) {
        this.siteDtos = siteDtos;
    }
}
