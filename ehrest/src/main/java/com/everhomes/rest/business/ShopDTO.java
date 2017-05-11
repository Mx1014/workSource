// @formatter:off
package com.everhomes.rest.business;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>shopNo: 店铺编号</li>
 * <li>shopName: 店铺名称</li>
 * <li>shopLogo: 店铺logo</li>
 * <li>appUserShopUrl: APP用户版店铺链接</li>
 * <li>searchTypeId: 内容类型id</li>
 * <li>searchTypeName: 内容类型名称</li>
 * <li>contentType: 内容类型</li>
 * </ul>
 */
public class ShopDTO {
	String shopNo;
	String shopName;
	String shopLogo;
	String appUserShopUrl;
	
	private Long searchTypeId;
	private String searchTypeName;
	private String contentType;
	
	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopLogo() {
		return shopLogo;
	}

	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}

	public String getAppUserShopUrl() {
		return appUserShopUrl;
	}

	public void setAppUserShopUrl(String appUserShopUrl) {
		this.appUserShopUrl = appUserShopUrl;
	}

	public Long getSearchTypeId() {
		return searchTypeId;
	}

	public void setSearchTypeId(Long searchTypeId) {
		this.searchTypeId = searchTypeId;
	}

	public String getSearchTypeName() {
		return searchTypeName;
	}

	public void setSearchTypeName(String searchTypeName) {
		this.searchTypeName = searchTypeName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
