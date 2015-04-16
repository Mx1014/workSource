// @formatter:off
package com.everhomes.group;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListMemberCommandResponse {
    private Long nextPageAnchor;
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
