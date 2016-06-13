package com.everhomes.rest.organization;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>orgId : 机构id</li>
 * 	<li>cityId : 城市id</li>
 * 	<li>areaId : 区县id</li>
 *	<li>address : 地址</li>
 *</ul>
 *
 */
public class AddOrgAddressCommand {
	@NotNull
	private Long orgId;
	@NotNull
	private Long cityId;
	@NotNull
	private Long areaId;
	@NotNull
	private String address;
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	

}
