// @formatter:off
package com.everhomes.rest.family;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>nextPageOffset：下一页取值</li>
 * <li>requests: 申请信息，参考{@link  com.everhomes.rest.family.FamilyDTO}</li>
 * </ul>
 */
public class ListWaitApproveFamilyCommandResponse {
    private Long nextPageOffset;
    
    @ItemType(FamilyDTO.class)
    private List<FamilyDTO> requests;
    
    public ListWaitApproveFamilyCommandResponse() {
    }

    public Long getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Long nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
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
