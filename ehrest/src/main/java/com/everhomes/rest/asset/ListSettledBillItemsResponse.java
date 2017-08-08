//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>nextPageAnchor:下一次的锚点</li>
 * <li>settledBillDTOS:已出账单的收费项目的集合，参考{@link com.everhomes.rest.asset.SettledBillDTO}</li>
 *</ul>
 */
public class ListSettledBillItemsResponse {
    private Long nextPageAnchor;
    @ItemType(SettledBillDTO.class)
    private List<SettledBillDTO> settledBillDTOS;

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

    public List<SettledBillDTO> getSettledBillDTOS() {
        return settledBillDTOS;
    }

    public void setSettledBillDTOS(List<SettledBillDTO> settledBillDTOS) {
        this.settledBillDTOS = settledBillDTOS;
    }

    public ListSettledBillItemsResponse() {

    }
}
