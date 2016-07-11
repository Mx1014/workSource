package com.everhomes.address;

import java.util.List;

import com.everhomes.util.StringHelper;

public class AddressDataInfo extends Address {
	
	/**
	 * 后台管理员导入公寓时解析的小区数据信息： 包含地址信息，地址的城市名称，区域名称和小区名称。
	 */
	private static final long serialVersionUID = 8766960150214094751L;
	//小区经纬度 
	/** 实体所属区域ID */
	private Long areaId;
	
	/** 实体所属城市名称 */
	private String cityName;

	/** 实体所属区县名称 */
	private String areaName;
	
	/** 实体所属小区名称 */
	private String CommunityName;
	
    public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
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

	public String getCommunityName() {
		return CommunityName;
	}

	public void setCommunityName(String communityName) {
		CommunityName = communityName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
