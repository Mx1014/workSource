package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 显示名称</li>
 *     <li>aliasName: 别名</li>
 *     <li>avatarUri: 头像Uri</li>
 *     <li>avatarUrl: 显示头像URL</li>
 *     <li>fullPinyin: 拼音全拼</li>
 *     <li>capitalPinyin: capitalPinyin</li>
 *     <li>organizationType: 公司类型 {@link OrganizationType}</li>
 *     <li>status: status  {@link OrganizationMemberStatus}</li>
 *     <li>siteDtos: 办公地点site列表 参考{@link OrganizationSiteDTO}</li>
 * </ul>
 */
public class OrganizationUserDTO {

    private Long id;
    private String name;
    private String aliasName;
    private String avatarUri;
    private String avatarUrl;
    private String fullPinyin;
    private String capitalPinyin;
    private Byte organizationType;
    private Byte status;
    private List<OrganizationSiteDTO> siteDtos;

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

    public Byte getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Byte organizationType) {
        this.organizationType = organizationType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public List<OrganizationSiteDTO> getSiteDtos() {
        return siteDtos;
    }

    public void setSiteDtos(List<OrganizationSiteDTO> siteDtos) {
        this.siteDtos = siteDtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
