package com.everhomes.rest.family;

import com.everhomes.rest.community.CommunityInfoDTO;
import com.everhomes.rest.group.GroupMemberStatus;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 名称</li>
 *     <li>aliasName: 别名</li>
 *     <li>avatarUri: 头像uri</li>
 *     <li>avatarUrl: 头像url</li>
 *     <li>fullPinyin: 拼音全拼</li>
 *     <li>capitalPinyin: 拼音首写字母</li>
 *     <li>status: status {@link GroupMemberStatus}</li>
 *     <li>community: 家庭所在园区 {@link com.everhomes.rest.community.CommunityInfoDTO}</li>
 * </ul>
 */
public class FamilyUserDTO {
    private Long id;
    private String name;
    private String aliasName;
    private String avatarUri;
    private String avatarUrl;
    private String fullPinyin;
    private String capitalPinyin;
    private Byte status;
    private CommunityInfoDTO community;

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public CommunityInfoDTO getCommunity() {
        return community;
    }

    public void setCommunity(CommunityInfoDTO community) {
        this.community = community;
    }
}