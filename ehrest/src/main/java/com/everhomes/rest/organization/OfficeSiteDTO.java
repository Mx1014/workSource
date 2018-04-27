package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
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
    private List<OrganizationApartDTO> siteDtos;

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

    public List<OrganizationApartDTO> getSiteDtos() {
        if(CollectionUtils.isEmpty(siteDtos)){
            return new ArrayList<OrganizationApartDTO>();
        }
        return siteDtos;
    }

    public void setSiteDtos(List<OrganizationApartDTO> siteDtos) {
        this.siteDtos = siteDtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
