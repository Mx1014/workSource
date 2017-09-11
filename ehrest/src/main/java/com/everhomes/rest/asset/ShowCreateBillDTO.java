//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>billGroupId:账单组Id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>billItemDTOList:账单组收费项目的集合，参考{@link com.everhomes.rest.asset.BillItemDTO}</li>
 *</ul>
 */
public class ShowCreateBillDTO {
    private Long billGroupId;
    private String billGroupName;
    @ItemType(BillItemDTO.class)
    private List<BillItemDTO> billItemDTOList;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
    }

    public List<BillItemDTO> getBillItemDTOList() {
        return billItemDTOList;
    }

    public void setBillItemDTOList(List<BillItemDTO> billItemDTOList) {
        this.billItemDTOList = billItemDTOList;
    }

    public ShowCreateBillDTO() {

    }
}
