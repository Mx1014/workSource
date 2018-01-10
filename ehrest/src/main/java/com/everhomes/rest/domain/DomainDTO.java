package com.everhomes.rest.domain;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>portalType: 门户类型，左邻域类型：zuolin，物业公司:pm 和企业：enterprise</li>
 *     <li>portalId: 配置类型，左邻域类型填0，物业公司和企业填具体id</li>
 *     <li>domain: 域名</li>
 *     <li>iconUri: iconUri</li>
 *     <li>iconUrl: iconUrl</li>
 *     <li>name: name</li>
 * </ul>
 */
public class DomainDTO {

	private Integer namespaceId;

	private String portalType;

	private Long portalId;

	private String domain;

	private String iconUri;

	private String iconUrl;

	private String name;

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

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
