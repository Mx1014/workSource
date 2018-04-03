// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>communityName : 项目名称</li>
 * <li>buildingName : 楼栋名称</li>
 * <li>apartmentName : 门牌名称</li>
 * </ul>
 *
 *  @author:dengs 2017年10月12日
 */
public class UnrentAddressDTO {
	private String communityName;
	private String buildingName;
	private String apartmentName;
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
