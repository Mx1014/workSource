package com.everhomes.rest.fixedasset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>dtos: 结果集，参考{@link com.everhomes.rest.fixedasset.FixedAssetDTO}</li>
 * <li>nextPageAnchor: 下一页开始的锚点</li>
 * </ul>
 */
public class ListFixedAssetResponse {
    @ItemType(FixedAssetDTO.class)
    private List<FixedAssetDTO> dtos;
    private Long nextPageAnchor;

    public List<FixedAssetDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<FixedAssetDTO> dtos) {
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
