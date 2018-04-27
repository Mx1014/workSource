package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>siteName: 办公地点名称</li>
 *     <li>communityName: 所属项目名称</li>
 *     <li>organizationApartDTOList: 楼栋和门牌的集合</li>
 * </ul>
 */
public class OfficeSiteDTO {

    private String siteName;
    private String communityName;
    private List<OrganizationApartDTO> organizationApartDTOList;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public List<OrganizationApartDTO> getOrganizationApartDTOList() {
        return organizationApartDTOList;
    }

    public void setOrganizationApartDTOList(List<OrganizationApartDTO> organizationApartDTOList) {
        this.organizationApartDTOList = organizationApartDTOList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
