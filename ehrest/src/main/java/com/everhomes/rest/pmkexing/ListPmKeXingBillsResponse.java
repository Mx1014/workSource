//@formatter:off
package com.everhomes.rest.pmkexing;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>nextPageOffset: 下一页页码</li>
 *     <li>bills: 账单列表 {@link com.everhomes.rest.pmkexing.PmKeXingBillDTO}</li>
 * </ul>
 */
public class ListPmKeXingBillsResponse {

    private Integer nextPageOffset;
    @ItemType(PmKeXingBillDTO.class)
    private List<PmKeXingBillDTO> bills = new ArrayList<>();

    public List<PmKeXingBillDTO> getBills() {
        return bills;
    }

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public void setBills(List<PmKeXingBillDTO> bills) {
        this.bills = bills;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
