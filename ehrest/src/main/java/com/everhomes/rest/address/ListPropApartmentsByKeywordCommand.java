// @formatter:off
package com.everhomes.rest.address;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>buildingName: 楼栋号</li>
 * <li>keyword: 查询关键字</li>
 * </ul>
 */
public class ListPropApartmentsByKeywordCommand {
    @NotNull
    private Long organizationId;
    
    private Long communityId;

	@NotNull
    private String buildingName;
	
	private String apartmentFloor;
    
    private String keyword;
    
    private Integer namespaceId;

    public ListPropApartmentsByKeywordCommand() {
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

	public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getApartmentFloor() {
		return apartmentFloor;
	}

	public void setApartmentFloor(String apartmentFloor) {
		this.apartmentFloor = apartmentFloor;
	}

	public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
