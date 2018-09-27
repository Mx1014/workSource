package com.everhomes.rest.address;

import com.everhomes.rest.organization.OrganizationSiteApartmentDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>
 * <li>siteName: 办公地点名称</li>
 * <li>communityId: 所在项目</li>
 * <li>provinceId: 省份编号</li>
 * <li>cityId: 城市ID</li>
 * <li>areaId: 区域Id</li>
 * <li>wholeAddressName: 地址全名</li>
 * <li>siteDtos: 关联的楼栋门牌，参考{@link OrganizationSiteApartmentDTO}</li>
 * </ul>
 */
public class CreateOfficeSiteCommand {
    private String siteName;
    private Long communityId;
    private Long provinceId;
    private Long cityId;
    private Long areaId;
    private String wholeAddressName;
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

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getWholeAddressName() {
        return wholeAddressName;
    }

    public void setWholeAddressName(String wholeAddressName) {
        this.wholeAddressName = wholeAddressName;
    }

    public List<OrganizationSiteApartmentDTO> getSiteDtos() {
        return siteDtos;
    }

    public void setSiteDtos(List<OrganizationSiteApartmentDTO> siteDtos) {
        this.siteDtos = siteDtos;
    }
}
