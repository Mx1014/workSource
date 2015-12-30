package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>groups: group信息，参考{@link com.everhomes.rest.group.GroupDTO}</li>
 * </ul>
 */
public class ListGroupCommandResponse {
    private Long nextPageAnchor;

    @ItemType(GroupDTO.class)
    private List<GroupDTO> groups;
    
    public ListGroupCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
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
