// @formatter:off
package com.everhomes.rest.openapi.jindi;

/**
 * 
 * <ul>金地同步数据的组织数据
 * <li>: </li>
 * </ul>
 */
public class JindiOrganizationDTO extends JindiDataDTO {
	private Long id;
	private String name;
	private Integer memberCount;
	private String address;

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
