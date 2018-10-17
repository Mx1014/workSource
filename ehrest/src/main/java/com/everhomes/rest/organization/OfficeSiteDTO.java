package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>siteName: 办公地点名称</li>
 *     <li>communityName: 所属项目名称</li>
 *     <li>communityId: communityId</li>
 *     <li>wholeAddressName: 办公地点名称全称</li>
 *     <li>siteDtos: siteDtos</li>
 * </ul>
 */
public class OfficeSiteDTO {

    private Long id;
    private String siteName;
    private String communityName;
    private Long communityId;
    private String wholeAddressName;
    private List<OrganizationApartDTO> siteDtos;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<OrganizationApartDTO> getSiteDtos() {
        if (CollectionUtils.isEmpty(siteDtos)) {
            return new ArrayList<OrganizationApartDTO>();
        }
        return siteDtos;
    }

    public void setSiteDtos(List<OrganizationApartDTO> siteDtos) {
        this.siteDtos = siteDtos;
    }

    public String getWholeAddressName() {
        return wholeAddressName;
    }

    public void setWholeAddressName(String wholeAddressName) {
        this.wholeAddressName = wholeAddressName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
