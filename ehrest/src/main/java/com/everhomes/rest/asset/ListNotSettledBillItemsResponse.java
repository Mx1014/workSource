//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>unsettledBillItemList:未出账单的收费项目列表，参考{@link com.everhomes.rest.asset.UnsettledBillItem}</li>
 * <li>nextPageAnchor:下一页的锚点</li>
 *</ul>
 */
public class ListNotSettledBillItemsResponse {
    @ItemType(UnsettledBillItem.class)
    private List<UnsettledBillItem> unsettledBillItemList;
    private Long nextPageAnchor;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<UnsettledBillItem> getUnsettledBillItemList() {
        return unsettledBillItemList;
    }

    public void setUnsettledBillItemList(List<UnsettledBillItem> unsettledBillItemList) {
        this.unsettledBillItemList = unsettledBillItemList;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public ListNotSettledBillItemsResponse() {

    }
}
