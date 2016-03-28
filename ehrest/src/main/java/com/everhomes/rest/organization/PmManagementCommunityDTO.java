package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>id: 小区Id</li>
 * <li>cityName: 城市名称</li>
 * <li>areaName: 区域名称</li>
 * <li>name: 小区名称</li>
 * <li>aliasName: 小区别名</li>
 * <li>address: 小区地址</li>
 * <li>aptCount: 公寓数</li>
 * <li>communityType: 园区类型，参考{@link com.everhomes.rest.community.CommunityType}</li>
 * <li>areaSize: 面积</li>
 * </ul>
 */
public class PmManagementCommunityDTO {

    private Long id;
    private String cityName;
    private String areaName;
    private String name;
    private String aliasName;
    private Integer aptCount;
    private String address;
    
    private Long creatorUid;
    private Byte communityType;
    private Double areaSize;
    
    private Byte isAll;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getAptCount() {
		return aptCount;
	}

	public void setAptCount(Integer aptCount) {
		this.aptCount = aptCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public Byte getCommunityType() {
		return communityType;
	}

	public void setCommunityType(Byte communityType) {
		this.communityType = communityType;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public Byte getIsAll() {
		return isAll;
	}

	public void setIsAll(Byte isAll) {
		this.isAll = isAll;
	}
    
    
}
