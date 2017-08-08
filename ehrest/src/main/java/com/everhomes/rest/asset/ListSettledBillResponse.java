//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>listSettledBillDTOs: 已出账单的收费项，参考{@link ListSettledBillDTO}</li>
 * <li>nextPageAnchor: 下一次锚点</li>
 *</ul>
 */
public class ListSettledBillResponse {
    @ItemType(ListSettledBillDTO.class)
    private List<ListSettledBillDTO> listSettledBillDTOs;
    private Long nextPageAnchor;

    public ListSettledBillResponse() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ListSettledBillDTO> getListSettledBillDTOs() {
        return listSettledBillDTOs;
    }

    public void setListSettledBillDTOs(List<ListSettledBillDTO> listSettledBillDTOs) {
        this.listSettledBillDTOs = listSettledBillDTOs;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
