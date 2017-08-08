//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>listSettledBillDTOs: 已出账单的减免项，参考{@link ListSettledBillExemptionItemsDTO}</li>
 * <li>nextPageAnchor:下一次的锚点</li>
 *</ul>
 */
public class ListSettledBillExemptionItemsResponse {
    @ItemType(ListSettledBillExemptionItemsDTO.class)
    private List<ListSettledBillExemptionItemsDTO> listNotSettledBillDTOs;
    private Long nextPageAnchor;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ListSettledBillExemptionItemsDTO> getListNotSettledBillDTOs() {
        return listNotSettledBillDTOs;
    }

    public void setListNotSettledBillDTOs(List<ListSettledBillExemptionItemsDTO> listNotSettledBillDTOs) {
        this.listNotSettledBillDTOs = listNotSettledBillDTOs;
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
