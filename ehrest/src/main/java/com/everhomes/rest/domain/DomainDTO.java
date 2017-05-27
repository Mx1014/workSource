package com.everhomes.rest.domain;

/**
 * <p>
 * 域名配置信息
 * </p>
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 配置类型，左邻域类型：EhZuolinAdmins，机构类型（包括物业公司和企业）：EhOrganizations</li>
 * <li>ownerId: 配置类型，左邻域类型填0，机构类型（包括物业公司和企业）填具体id</li>
 * <li>domain: 域名</li>
 * <ul>
 */

public class DomainDTO {

	private Integer namespaceId;

	private String ownerType;

	private Long ownerId;

	private String domain;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
