// @formatter:off
package com.everhomes.family;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>pageOffset：下一页取值</li>
 * <li>requests: 申请信息，参考{@link  com.everhomes.family.FamilyDTO}</li>
 * </ul>
 */
public class ListWaitApproveFamilyCommandResponse {
    private Long pageOffset;
    
    @ItemType(FamilyDTO.class)
    private List<FamilyDTO> requests;
    
    public ListWaitApproveFamilyCommandResponse() {
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }

    public List<FamilyDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<FamilyDTO> requests) {
        this.requests = requests;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
