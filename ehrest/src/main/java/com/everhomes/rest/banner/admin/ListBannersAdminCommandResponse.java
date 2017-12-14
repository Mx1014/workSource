package com.everhomes.rest.banner.admin;


import com.everhomes.discover.ItemType;
import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>keyword: 查询banner关键字</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListBannersAdminCommandResponse {
    
    @ItemType(BannerDTO.class)
    private List<BannerDTO> requests;
    
    private Long nextPageAnchor;
    
    public List<BannerDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<BannerDTO> requests) {
        this.requests = requests;
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
