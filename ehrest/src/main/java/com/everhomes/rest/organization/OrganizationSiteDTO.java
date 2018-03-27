package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.util.List;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 显示名称</li>
 *     <li>communityId: 所在园区Id</li>
 *     <li>communityName: 所在园区名称</li>
 *     <li>xxx: 待敢哥补充</li>
 * </ul>
 */
public class OrganizationSiteDTO {

    private Long id;
    private String name;
    private Long communityId;
    private String communityName;

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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
