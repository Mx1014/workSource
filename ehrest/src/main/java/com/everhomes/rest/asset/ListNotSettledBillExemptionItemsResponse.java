//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>unsettledBillExemptionItemList:未出账单的收费项目列表，参考{@link com.everhomes.rest.asset.UnsettledBillExemptionItem}</li>
 * <li>nextPageAnchor:下一页的锚点</li>
 *</ul>
 */
public class ListNotSettledBillExemptionItemsResponse {
    @ItemType(UnsettledBillExemptionItem.class)
    private List<UnsettledBillExemptionItem> unsettledBillExemptionItemList;
    private Long nextPageAnchor;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<UnsettledBillExemptionItem> getUnsettledBillExemptionItemList() {
        return unsettledBillExemptionItemList;
    }

    public void setUnsettledBillExemptionItemList(List<UnsettledBillExemptionItem> unsettledBillExemptionItemList) {
        this.unsettledBillExemptionItemList = unsettledBillExemptionItemList;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public ListNotSettledBillExemptionItemsResponse() {

    }
}
