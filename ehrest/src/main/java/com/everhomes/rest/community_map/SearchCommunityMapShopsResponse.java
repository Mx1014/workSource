package com.everhomes.rest.community_map;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * @author sw on 2017/9/25.
 */
public class SearchCommunityMapShopsResponse {
    private Long nextPageAnchor;
    @ItemType(CommunityMapShopDetailDTO.class)
    private List<CommunityMapShopDetailDTO> shops;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<CommunityMapShopDetailDTO> getShops() {
        return shops;
    }

    public void setShops(List<CommunityMapShopDetailDTO> shops) {
        this.shops = shops;
    }
}
