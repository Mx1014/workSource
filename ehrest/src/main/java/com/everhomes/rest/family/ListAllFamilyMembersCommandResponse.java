// @formatter:off
package com.everhomes.rest.family;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>nextPageOffset：下一页页码</li>
 * <li>requests: 成员列表，参考{@link  com.everhomes.rest.family.FamilyMemberFullDTO}</li>
 * </ul>
 */
public class ListAllFamilyMembersCommandResponse {
    private Integer nextPageOffset;
    
    @ItemType(FamilyMemberFullDTO.class)
    private List<FamilyMemberFullDTO> requests;
    
    public ListAllFamilyMembersCommandResponse() {
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<FamilyMemberFullDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<FamilyMemberFullDTO> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
