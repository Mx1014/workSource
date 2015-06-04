// @formatter:off
package com.everhomes.family;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>requests: 成员列表，参考{@link  com.everhomes.family.FamilyMemberFullDTO}</li>
 * </ul>
 */
public class ListAllFamilyMembersCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(FamilyMemberFullDTO.class)
    private List<FamilyMemberFullDTO> requests;
    
    public ListAllFamilyMembersCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
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
