//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>nextPageAnchor:下一次的锚点</li>
 * <li>billDTOS:已出账单的收费项目的集合，参考{@link BillDTO}</li>
 *</ul>
 */
public class ListBillItemsResponse {
    private Long nextPageAnchor;
    @ItemType(BillDTO.class)
    private List<BillDTO> billDTOS;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<BillDTO> getBillDTOS() {
        return billDTOS;
    }

    public void setBillDTOS(List<BillDTO> billDTOS) {
        this.billDTOS = billDTOS;
    }

    public ListBillItemsResponse() {

    }
}
