// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>列出组相关的可处理的被邀请请求列表</p>
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>requests: 申请信息，参考{@link com.everhomes.rest.group.GroupMemberDTO}</li>
 * </ul>
 */
public class ListGroupWaitingApprovalsCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(GroupMemberDTO.class)
    private List<GroupMemberDTO> requests;
    
    public ListGroupWaitingApprovalsCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GroupMemberDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<GroupMemberDTO> requests) {
        this.requests = requests;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
