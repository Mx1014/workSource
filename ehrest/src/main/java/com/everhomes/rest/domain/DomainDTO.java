package com.everhomes.rest.domain;

/**
 * <p>
 * 域名配置信息
 * </p>
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>portalType: 门户类型，左邻域类型：zuolin，物业公司:pm 和企业：enterprise</li>
 * <li>portalId: 配置类型，左邻域类型填0，物业公司和企业填具体id</li>
 * <li>domain: 域名</li>
 * <ul>
 */

public class DomainDTO {

	private Integer namespaceId;

	private String portalType;

	private Long portalId;

	private String domain;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public String getPortalType() {
		return portalType;
	}

	public void setPortalType(String portalType) {
		this.portalType = portalType;
	}

	public Long getPortalId() {
		return portalId;
	}

	public void setPortalId(Long portalId) {
		this.portalId = portalId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
