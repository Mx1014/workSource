//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/10/26.
 */
/**
 *<ul>
 * <li>list:参考{@link com.everhomes.rest.asset.ListChargingItemsForBillGroupDTO}</li>
 * <li>nextPageAnchor:下一个锚点</li>
 *</ul>
 */
public class ListChargingItemsForBillGroupResponse {
    @ItemType(ListChargingItemsForBillGroupDTO.class)
    private List<ListChargingItemsForBillGroupDTO> list;
    private Long nextPageAnchor;

    public List<ListChargingItemsForBillGroupDTO> getList() {
        return list;
    }

    public void setList(List<ListChargingItemsForBillGroupDTO> list) {
        this.list = list;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
