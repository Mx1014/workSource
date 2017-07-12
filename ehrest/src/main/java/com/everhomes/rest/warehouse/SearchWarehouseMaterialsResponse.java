package com.everhomes.rest.warehouse;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: 下一页锚点</li>
 *     <li>materialDTOs: 物品列表 参考{@link com.everhomes.rest.warehouse.WarehouseMaterialDTO}</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class SearchWarehouseMaterialsResponse {
    private Long nextPageAnchor;

    @ItemType(WarehouseMaterialDTO.class)
    private List<WarehouseMaterialDTO> materialDTOs;

    public List<WarehouseMaterialDTO> getMaterialDTOs() {
        return materialDTOs;
    }

    public void setMaterialDTOs(List<WarehouseMaterialDTO> materialDTOs) {
        this.materialDTOs = materialDTOs;
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
