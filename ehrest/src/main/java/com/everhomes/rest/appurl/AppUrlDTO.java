package com.everhomes.rest.appurl;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>name: 应用名称</li>
 *  <li>description: 应用描述</li>
 *  <li>logoUrl: logo image url</li>
 *  <li>downloadUrl: 应用商店链接</li>
 *  <li>themeColor: 主题色</li>
 *  <li>linkUrl: 跳转链接URL</li>
 * </ul>
 *
 */
public class AppUrlDTO {
	
	private String name;
	
	private String description;
	
	private String logoUrl;
	
	private String downloadUrl;

	private String themeColor;

	private String linkUrl;

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getLogoUrl() {
		return logoUrl;
	}


	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}


	public String getDownloadUrl() {
		return downloadUrl;
	}


	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
