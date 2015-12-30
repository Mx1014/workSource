package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageOffset：下一页页码，如果没有则后面没有数据</li>
 * <li>groups: group信息，参考{@link com.everhomes.rest.group.GroupDTO}</li>
 * </ul>
 */
public class ListNearbyGroupCommandResponse {
    private Integer nextPageOffset;

    @ItemType(GroupDTO.class)
    private List<GroupDTO> groups;
    
    public ListNearbyGroupCommandResponse() {
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDTO> groups) {
        this.groups = groups;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
