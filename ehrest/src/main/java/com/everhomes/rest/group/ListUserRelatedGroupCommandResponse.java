// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>group信息
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>requests: group信息，参考{@link com.everhomes.rest.group.GroupDTO}</li>
 * </ul>
 */
public class ListUserRelatedGroupCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(GroupDTO.class)
    private List<GroupDTO> requests;
    
    public ListUserRelatedGroupCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GroupDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<GroupDTO> requests) {
        this.requests = requests;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
