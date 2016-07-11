package com.everhomes.community;

import java.util.List;

import com.everhomes.util.StringHelper;

public class CommunityDataInfo extends Community {
	
	/**
	 * 后台管理员导入小区时解析的小区数据信息： 包含小区信息，小区经纬度，物业电话列表 
	 */
	private static final long serialVersionUID = 8766960150214094751L;
	//小区经纬度 
	private java.lang.Double longitude;
	private java.lang.Double latitude;
	
	//物业电话列表
	private List<String> phones;

	public java.lang.Double getLongitude() {
		return longitude;
	}

	public void setLongitude(java.lang.Double longitude) {
		this.longitude = longitude;
	}

	public java.lang.Double getLatitude() {
		return latitude;
	}

	public void setLatitude(java.lang.Double latitude) {
		this.latitude = latitude;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public CommunityDataInfo() {
		
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
