//@formatter:off
package com.everhomes.rest.asset.bill;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>listBillsDTOS: 账单的集合，参考{@link ListBillsDTO}</li>
 * <li>nextPageAnchor: 下一次锚点</li>
 *</ul>
 */
public class ListBillsResponse {
    @ItemType(ListBillsDTO.class)
    private List<ListBillsDTO> listBillsDTOS;
    private Long nextPageAnchor;

    public ListBillsResponse() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<ListBillsDTO> getListBillsDTOS() {
        return listBillsDTOS;
    }

    public void setListBillsDTOS(List<ListBillsDTO> listBillsDTOS) {
        this.listBillsDTOS = listBillsDTOS;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
