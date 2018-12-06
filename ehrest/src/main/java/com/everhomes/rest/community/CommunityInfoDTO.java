// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 显示名称</li>
 *     <li>aliasName: 别名</li>
 *     <li>avatarUri: 显示头像URI</li>
 *     <li>avatarUrl: 显示头像URL</li>
 *     <li>communityType: 园区类型参考{@link com.everhomes.rest.community.CommunityType}</li>
 *     <li>fullPinyin: 拼音全拼</li>
 *     <li>capitalPinyin: capitalPinyin</li>
 *     <li>siteFlag: 办公标志 参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>siteAddress: 办公地址</li>
 *     <li>apartmentFlag: 公寓标志 参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>apartmentAddress: 公寓地址</li>
 *     <li>backgroundImgUrl: 小区或园区项目的图片链接</li>
 * </ul>
 */
public class CommunityInfoDTO {
    private Long id;
    private String name;
    private String aliasName;
    private String avatarUri;
    private String avatarUrl;
    private Byte communityType;
    private String fullPinyin;
    private String capitalPinyin;
    private Byte siteFlag;
    private String siteAddress;
    private Byte apartmentFlag;
    private String apartmentAddress;
    private String backgroundImgUrl;

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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    public String getFullPinyin() {
        return fullPinyin;
    }

    public void setFullPinyin(String fullPinyin) {
        this.fullPinyin = fullPinyin;
    }

    public String getCapitalPinyin() {
        return capitalPinyin;
    }

    public void setCapitalPinyin(String capitalPinyin) {
        this.capitalPinyin = capitalPinyin;
    }

    public Byte getSiteFlag() {
        return siteFlag;
    }

    public void setSiteFlag(Byte siteFlag) {
        this.siteFlag = siteFlag;
    }

    public Byte getApartmentFlag() {
        return apartmentFlag;
    }

    public void setApartmentFlag(Byte apartmentFlag) {
        this.apartmentFlag = apartmentFlag;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getApartmentAddress() {
        return apartmentAddress;
    }

    public void setApartmentAddress(String apartmentAddress) {
        this.apartmentAddress = apartmentAddress;
    }
    
	public String getBackgroundImgUrl() {
		return backgroundImgUrl;
	}

	public void setBackgroundImgUrl(String backgroundImgUrl) {
		this.backgroundImgUrl = backgroundImgUrl;
	}
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
