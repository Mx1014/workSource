// @formatter:off
package com.everhomes.community;

import java.util.List;

import com.everhomes.address.CommunityDTO;
import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>requests: 申请信息，参考{@link com.everhomes.address.CommunityDTO}</li>
 * </ul>
 */
public class ListWaitingForCommunitesCommandResponse {
    
    private Long nextPageAnchor;
    
    private List<CommunityDTO> requests;
    
    public ListWaitingForCommunitesCommandResponse() {
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<CommunityDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<CommunityDTO> requests) {
        this.requests = requests;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
