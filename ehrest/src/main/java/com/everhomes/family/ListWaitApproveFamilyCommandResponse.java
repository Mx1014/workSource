// @formatter:off
package com.everhomes.family;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>requests: 申请信息，参考{@link  com.everhomes.family.FamilyDTO}</li>
 * </ul>
 */
public class ListWaitApproveFamilyCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(FamilyDTO.class)
    private List<FamilyDTO> requests;
    
    public ListWaitApproveFamilyCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
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
