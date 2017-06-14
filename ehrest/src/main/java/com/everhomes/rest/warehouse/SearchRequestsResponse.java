package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>WarehouseRequestMaterialDTO: 申请列表 参考{@link WarehouseRequestMaterialDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class SearchRequestsResponse {

    private Long nextPageAnchor;

    @ItemType(WarehouseRequestMaterialDTO.class)
    private List<WarehouseRequestMaterialDTO> requestDTOs;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<WarehouseRequestMaterialDTO> getRequestDTOs() {
        return requestDTOs;
    }

    public void setRequestDTOs(List<WarehouseRequestMaterialDTO> requestDTOs) {
        this.requestDTOs = requestDTOs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
