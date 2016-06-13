// @formatter:off
package com.everhomes.rest.namespace;

import java.util.List;

import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>communities：小区/园区信息，参考{@link com.everhomes.rest.address.CommunityDTO}</li>
 * </ul>
 */
public class ListCommunityByNamespaceCommandResponse {
    private Long nextPageAnchor;
    
    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> communities;
    
    public ListCommunityByNamespaceCommandResponse() {
    }
    
    public ListCommunityByNamespaceCommandResponse(Long nextPageAnchor, List<CommunityDTO> communities) {
        this.nextPageAnchor = nextPageAnchor;
        this.communities = communities;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
    
    public List<CommunityDTO> getCommunities() {
        return communities;
    }

    public void setCommunities(List<CommunityDTO> communities) {
        this.communities = communities;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
