package com.everhomes.rest.banner.admin;


import java.util.List;

import com.everhomes.rest.banner.BannerDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>keyword: 查询banner关键字</li>
 * <li>nextPageOffset: 下一页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListBannersAdminCommandResponse {
    
    @ItemType(BannerDTO.class)
    private List<BannerDTO> requests;
    
    private Integer nextPageOffset;
    
    public List<BannerDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<BannerDTO> requests) {
        this.requests = requests;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
