// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>成为group管理员的申请信息
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>requests: 申请信息，参考{@link com.everhomes.rest.group.GroupOpRequestDTO}</li>
 * </ul>
 */
public class ListAdminOpRequestCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(GroupOpRequestDTO.class)
    private List<GroupOpRequestDTO> requests;
    
    public ListAdminOpRequestCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GroupOpRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<GroupOpRequestDTO> requests) {
        this.requests = requests;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
