package com.everhomes.rest.varField;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 项目id</li>
 *     <li>moduleName: 组所属的模块类型名</li>
 *     <li>groups: 字段组信息， 参考{@link com.everhomes.rest.varField.ScopeFieldGroupInfo}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class UpdateFieldGroupsCommand {
    private Integer namespaceId;

    private Long communityId;

    private String moduleName;

    @ItemType(ScopeFieldGroupInfo.class)
    private List<ScopeFieldGroupInfo> groups;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<ScopeFieldGroupInfo> getGroups() {
        return groups;
    }

    public void setGroups(List<ScopeFieldGroupInfo> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

