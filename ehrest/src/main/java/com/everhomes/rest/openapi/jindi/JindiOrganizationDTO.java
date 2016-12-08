// @formatter:off
package com.everhomes.rest.openapi.jindi;

import java.sql.Timestamp;

/**
 * 
 * <ul>金地同步数据的组织数据
 * <li>: </li>
 * </ul>
 */
public class JindiOrganizationDTO {
	private Long id;
	private String name;
	private Integer memberCount;
	private String address;
	private Timestamp createTime;
	private Timestamp updateTime;

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Integer memberCount) {
		this.memberCount = memberCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
