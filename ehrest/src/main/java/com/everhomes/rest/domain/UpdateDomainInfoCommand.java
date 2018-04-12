package com.everhomes.rest.domain;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>faviconUri: 标签页iconuri</li>
 *     <li>loginBgUri: 登录页BgUri</li>
 *     <li>loginLogoUri: 登录页LogoUri</li>
 *     <li>menuLogoUri: 菜单LogoUri</li>
 *     <li>menuLogoCollapsedUri: 菜单关闭Uri</li>
 * </ul>
 */
public class UpdateDomainInfoCommand {

	private Long id;

	private String faviconUri;

	private String loginBgUri;

	private String loginLogoUri;

	private String menuLogoUri;

	private String menuLogoCollapsedUri;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFaviconUri() {
		return faviconUri;
	}

	public void setFaviconUri(String faviconUri) {
		this.faviconUri = faviconUri;
	}

	public String getLoginBgUri() {
		return loginBgUri;
	}

	public void setLoginBgUri(String loginBgUri) {
		this.loginBgUri = loginBgUri;
	}

	public String getLoginLogoUri() {
		return loginLogoUri;
	}

	public void setLoginLogoUri(String loginLogoUri) {
		this.loginLogoUri = loginLogoUri;
	}

	public String getMenuLogoUri() {
		return menuLogoUri;
	}

	public void setMenuLogoUri(String menuLogoUri) {
		this.menuLogoUri = menuLogoUri;
	}

	public String getMenuLogoCollapsedUri() {
		return menuLogoCollapsedUri;
	}

	public void setMenuLogoCollapsedUri(String menuLogoCollapsedUri) {
		this.menuLogoCollapsedUri = menuLogoCollapsedUri;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
