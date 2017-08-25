//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>listSettledBillDTOs: 账单的减免项，参考{@link ListBillExemptionItemsDTO}</li>
 * <li>nextPageAnchor:下一次的锚点</li>
 *</ul>
 */
public class ListSettledBillExemptionItemsResponse {
    @ItemType(ListBillExemptionItemsDTO.class)
    private List<ListBillExemptionItemsDTO> billDTOS;
    private Long nextPageAnchor;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ListBillExemptionItemsDTO> getBillDTOS() {
        return billDTOS;
    }

    public void setBillDTOS(List<ListBillExemptionItemsDTO> billDTOS) {
        this.billDTOS = billDTOS;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public ListSettledBillExemptionItemsResponse() {

    }
}
