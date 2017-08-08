//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billGroupDTOList:账单组列表，参考{@link com.everhomes.rest.asset.BillGroupDTO}，此返回中exemptionItemDTOList为空</li>
 *</ul>
 */
public class ShowCreateBillDTO {
    @ItemType(BillGroupDTO.class)
    private List<BillGroupDTO> billGroupDTOList;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<BillGroupDTO> getBillGroupDTOList() {
        return billGroupDTOList;
    }

    public void setBillGroupDTOList(List<BillGroupDTO> billGroupDTOList) {
        this.billGroupDTOList = billGroupDTOList;
    }

    public ShowCreateBillDTO() {

    }
}
