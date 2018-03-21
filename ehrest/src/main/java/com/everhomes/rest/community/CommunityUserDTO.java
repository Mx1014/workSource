// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 显示名称</li>
 *     <li>aliasName: 别名</li>
 *     <li>avatarUri: 显示头像URI</li>
 *     <li>avatarUrl: 显示头像URL</li>
 *     <li>communityType: 园区类型</li>
 *     <li>fullPinyin: 拼音全拼</li>
 *     <li>capitalPinyin: capitalPinyin</li>
 *     <li>managementFlag: 管理项目标志 参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>siteFlag: 办公标志 参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class CommunityUserDTO {
    private Long id;
    private String name;
    private String aliasName;
    private String avatarUri;
    private String avatarUrl;
    private Byte communityType;
    private String fullPinyin;
    private String capitalPinyin;
    private Byte managementFlag;
    private Byte siteFlag;

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

    public Byte getManagementFlag() {
        return managementFlag;
    }

    public void setManagementFlag(Byte managementFlag) {
        this.managementFlag = managementFlag;
    }

    public Byte getSiteFlag() {
        return siteFlag;
    }

    public void setSiteFlag(Byte siteFlag) {
        this.siteFlag = siteFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
