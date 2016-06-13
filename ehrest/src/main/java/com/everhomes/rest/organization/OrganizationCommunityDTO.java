// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 * <li>organizationId：机构id</li>
 * <li>communityId：小区id</li>
 * <li>communityName：小区名称</li>
 * <li>cityName：城市名称</li>
 * <li>areaName：区域名称</li>
 * <li>status：小区状态,参考:{@link com.everhomes.rest.address.CommunityAdminStatus}</li>	
 * <li>communityType: 园区类型，参考{@link com.everhomes.rest.community.CommunityType}</li>
 * </ul>
 */
public class OrganizationCommunityDTO {
	
	private Long id;
	private Long organizationId;
	private Long communityId;
	private String communityName;
	private String cityName;
	private String areaName;
	private Byte status;
    private Byte communityType;
	
	public OrganizationCommunityDTO() {
		
	}
	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
