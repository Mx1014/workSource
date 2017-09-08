package com.everhomes.rest.address;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>dtos: 参考{@link com.everhomes.rest.address.CommunityDTO}</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListNearbyMixCommunitiesCommandResponse extends RestResponseBase {

    @ItemType(CommunityDTO.class)
    private List<CommunityDTO> dtos;

    private Long nextPageAnchor;

    public List<CommunityDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CommunityDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
