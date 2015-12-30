// @formatter:off
package com.everhomes.rest.family;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>requests: 申请信息，参考{@link  com.everhomes.rest.family.FamilyMembershipRequestDTO}</li>
 * </ul>
 */
public class ListFamilyRequestsCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(FamilyMembershipRequestDTO.class)
    private List<FamilyMembershipRequestDTO> requests;
    
    public ListFamilyRequestsCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    
    public List<FamilyMembershipRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<FamilyMembershipRequestDTO> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
