package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>bills: 账单列表 参考{@link com.everhomes.rest.asset.SimpleAssetBillDTO}</li>
 *     <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListSimpleAssetBillsResponse {

    @ItemType(SimpleAssetBillDTO.class)
    private List<SimpleAssetBillDTO> bills;

    private Long nextPageAnchor;

    public List<SimpleAssetBillDTO> getBills() {
        return bills;
    }

    public void setBills(List<SimpleAssetBillDTO> bills) {
        this.bills = bills;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
