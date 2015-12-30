// @formatter:off
package com.everhomes.rest.group;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>members：group成员信息，参考{@link com.everhomes.rest.group.GroupMemberDTO}</li>
 * </ul>
 */
public class ListMemberCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(GroupMemberDTO.class)
    private List<GroupMemberDTO> members;
    
    public ListMemberCommandResponse() {
    }
    
    public ListMemberCommandResponse(Long nextPageAnchor, List<GroupMemberDTO> members) {
        this.nextPageAnchor = nextPageAnchor;
        this.members = members;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<GroupMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberDTO> members) {
        this.members = members;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
