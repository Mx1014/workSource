package com.everhomes.rest.field;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>groups: 字段组信息， 参考{@link com.everhomes.rest.field.ScopeFieldGroupInfo}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class UpdateFieldGroupsCommand {
    @ItemType(ScopeFieldGroupInfo.class)
    private List<ScopeFieldGroupInfo> groups;

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

