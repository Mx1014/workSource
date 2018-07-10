// @formatter:off
package com.everhomes.rest.community.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.rest.community.CommunityGeoPointDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 被更新的小区Id</li>
 * <li>address: 小区地址</li>
 * <li>cityId: 小区所在城市IdId</li>
 * <li>areaId: 小区所在城市区县Id</li>
 * <li>areaSize: 面积</li>
 * <li>geoPointList: 小区经纬度列表,参考{@link com.everhomes.rest.community.CommunityGeoPointDTO}</li>
 * <li>communityNumber: 园区编号</li>
 * <li>aliasName: 简称</li>
 * <li>name: 项目名称</li>
 * </ul>
 */
public class UpdateCommunityAdminCommand {
    @NotNull
    private Long communityId;
    
    private String address;
    @NotNull
    private Long cityId;
    @NotNull
    private Long areaId;
    
    private Integer namespaceId;
    
    private Long id;
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	private Double areaSize;
    
    @ItemType(CommunityGeoPointDTO.class)
    private List<CommunityGeoPointDTO> geoPointList;

    private String communityNumber;

    private String aliasName;
    private String name;

    public UpdateCommunityAdminCommand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    
    public List<CommunityGeoPointDTO> getGeoPointList() {
		return geoPointList;
	}

	public void setGeoPointList(List<CommunityGeoPointDTO> geoPointList) {
		this.geoPointList = geoPointList;
	}
	
	

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

    public String getCommunityNumber() {
        return communityNumber;
    }

    public void setCommunityNumber(String communityNumber) {
        this.communityNumber = communityNumber;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
