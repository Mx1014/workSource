// @formatter:off
package com.everhomes.pm;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，本次开始取的位置</li>
 * <li>members：pmMember成员信息，参考{@link com.everhomes.pm.PropertyMemberDTO}</li>
 * </ul>
 */
public class ListPropMemberCommandResponse {
    private Long nextPageAnchor;
    private List<PropertyMemberDTO> members;
    
    public ListPropMemberCommandResponse() {
    }
    
    public ListPropMemberCommandResponse(Long nextPageAnchor, List<PropertyMemberDTO> members) {
        this.nextPageAnchor = nextPageAnchor;
        this.members = members;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PropertyMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<PropertyMemberDTO> members) {
        this.members = members;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
