package com.everhomes.rest.activity;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>goods: 附件列表 参考{@link com.everhomes.rest.activity.ActivityGoodsDTO}</li>
 *     <li>nextPageAnchor: 下一页的锚点</li>
 * </ul>
 */
public class ListActivityGoodsResponse {

    @ItemType(ActivityGoodsDTO.class)
    private List<ActivityGoodsDTO> goods;

    private Long nextPageAnchor;

    public List<ActivityGoodsDTO> getGoods() {
        return goods;
    }

    public void setGoods(List<ActivityGoodsDTO> goods) {
        this.goods = goods;
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
