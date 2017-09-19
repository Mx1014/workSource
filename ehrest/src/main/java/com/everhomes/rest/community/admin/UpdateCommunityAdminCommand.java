// @formatter:off
package com.everhomes.rest.community.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.rest.community.CommunityGeoPointDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>name: 小区名称</li>
 * <li>aliasName: 小区别称</li>
 * <li>communityId: 被更新的小区Id</li>
 * <li>address: 小区地址</li>
 * <li>cityId: 小区所在城市IdId</li>
 * <li>areaId: 小区所在城市区县Id</li>
 * <li>areaSize: 面积</li>
 * <li>geoPointList: 小区经纬度列表,参考{@link com.everhomes.rest.community.CommunityGeoPointDTO}</li>
 * </ul>
 */
public class UpdateCommunityAdminCommand {

    private String name;

    private String aliasName;

    @NotNull
    private Long communityId;
    
    private String address;
    @NotNull
    private Long cityId;
    @NotNull
    private Long areaId;
    
    private Double areaSize;
    
    @ItemType(CommunityGeoPointDTO.class)
    private List<CommunityGeoPointDTO> geoPointList;
    
    public UpdateCommunityAdminCommand() {
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
