package com.everhomes.rest.domain;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>Id: Id</li>
 *     <li>namespaceId: 域空间</li>
 *     <li>portalType: 门户类型，左邻域类型：zuolin，物业公司:pm 和企业：enterprise</li>
 *     <li>portalId: 配置类型，左邻域类型填0，物业公司和企业填具体id</li>
 *     <li>domain: 域名</li>
 *     <li>faviconUri: 标签页iconuri</li>
 *     <li>faviconUrl: 标签页iconUrl</li>
 *     <li>name: name</li>
 *     <li>loginBgUri: 登录页BgUri</li>
 *     <li>loginBgUrl: 登录页BgUrl</li>
 *     <li>loginLogoUri: 登录页LogoUri</li>
 *     <li>loginLogoUrl: 登录页LogoUrl</li>
 *     <li>menuLogoUri: 菜单LogoUri</li>
 *     <li>menuLogoUrl: 菜单LogoUrl</li>
 *     <li>menuLogoCollapsedUri: 菜单关闭Uri</li>
 *     <li>menuLogoCollapsedUrl: 菜单关闭Url</li>
 * </ul>
 */
public class DomainDTO {

	private Long Id;

	private Integer namespaceId;

	private String portalType;

	private Long portalId;

	private String domain;

	private String faviconUri;

	private String faviconUrl;

	private String name;

	private String loginBgUri;

	private String loginBgUrl;

	private String loginLogoUri;

	private String loginLogoUrl;

	private String menuLogoUri;

	private String menuLogoUrl;

	private String menuLogoCollapsedUri;

	private String menuLogoCollapsedUrl;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

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


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFaviconUri() {
		return faviconUri;
	}

	public void setFaviconUri(String faviconUri) {
		this.faviconUri = faviconUri;
	}

	public String getFaviconUrl() {
		return faviconUrl;
	}

	public void setFaviconUrl(String faviconUrl) {
		this.faviconUrl = faviconUrl;
	}

	public String getLoginBgUri() {
		return loginBgUri;
	}

	public void setLoginBgUri(String loginBgUri) {
		this.loginBgUri = loginBgUri;
	}

	public String getLoginBgUrl() {
		return loginBgUrl;
	}

	public void setLoginBgUrl(String loginBgUrl) {
		this.loginBgUrl = loginBgUrl;
	}

	public String getLoginLogoUri() {
		return loginLogoUri;
	}

	public void setLoginLogoUri(String loginLogoUri) {
		this.loginLogoUri = loginLogoUri;
	}

	public String getLoginLogoUrl() {
		return loginLogoUrl;
	}

	public void setLoginLogoUrl(String loginLogoUrl) {
		this.loginLogoUrl = loginLogoUrl;
	}

	public String getMenuLogoUri() {
		return menuLogoUri;
	}

	public void setMenuLogoUri(String menuLogoUri) {
		this.menuLogoUri = menuLogoUri;
	}

	public String getMenuLogoUrl() {
		return menuLogoUrl;
	}

	public void setMenuLogoUrl(String menuLogoUrl) {
		this.menuLogoUrl = menuLogoUrl;
	}

	public String getMenuLogoCollapsedUri() {
		return menuLogoCollapsedUri;
	}

	public void setMenuLogoCollapsedUri(String menuLogoCollapsedUri) {
		this.menuLogoCollapsedUri = menuLogoCollapsedUri;
	}

	public String getMenuLogoCollapsedUrl() {
		return menuLogoCollapsedUrl;
	}

	public void setMenuLogoCollapsedUrl(String menuLogoCollapsedUrl) {
		this.menuLogoCollapsedUrl = menuLogoCollapsedUrl;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
