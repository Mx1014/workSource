package com.everhomes.rest.openapi;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>communityId:园区id</li>
 *	<li>name：园区名字</li>
 *	<li>address：园区地址</li>
 *	<li>description：描述</li>
 * <ul>
 *
 */
public class CommunityDTO {
	
	private Long communityId;
	private String name;
	private String address;
	private String description;
	
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
