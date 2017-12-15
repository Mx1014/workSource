package com.everhomes.rest.point;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 *     <li>goods: 商品列表 {@link com.everhomes.rest.point.PointLogDTO}</li>
 * </ul>
 */
public class ListPointGoodsResponse {

    private Long nextPageAnchor;

    @ItemType(PointLogDTO.class)
    private List<PointGoodDTO> goods;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<PointGoodDTO> getGoods() {
        return goods;
    }

    public void setGoods(List<PointGoodDTO> goods) {
        this.goods = goods;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
