package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * <li>name: 业务模块 ID</li>
 * <li>defaultValue: 默认值，可以为空（用户可以再次选择）</li>
 * <li>title: 标题 </li>
 * <li>routerUrl: 可以为空，没有则无法路由跳转</li>
 * </ul>
 * @author janson
 *
 */
public class SmartCardHandlerItem {
	private String name;
	private String defaultValue;
	private String title;
	private String routerUrl;
	private String iconUrl;
	private String iconUri;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRouterUrl() {
		return routerUrl;
	}

	public void setRouterUrl(String routerUrl) {
		this.routerUrl = routerUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
