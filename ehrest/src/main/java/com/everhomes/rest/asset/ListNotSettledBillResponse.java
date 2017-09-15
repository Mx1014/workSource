//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>listNotSettledBillDTOs: 未出账单列表，参考{@link com.everhomes.rest.asset.ListNotSettledBillDTO}</li>
 * <li>nextPageAnchor:下一页的锚点</li>
 *</ul>
 */
public class ListNotSettledBillResponse {
    @ItemType(ListNotSettledBillDTO.class)
    private List<ListNotSettledBillDTO> listNotSettledBillDTOs;

    private Long nextPageAnchor;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ListNotSettledBillDTO> getListNotSettledBillDTOs() {
        return listNotSettledBillDTOs;
    }

    public void setListNotSettledBillDTOs(List<ListNotSettledBillDTO> listNotSettledBillDTOs) {
        this.listNotSettledBillDTOs = listNotSettledBillDTOs;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public ListNotSettledBillResponse() {

    }
}
