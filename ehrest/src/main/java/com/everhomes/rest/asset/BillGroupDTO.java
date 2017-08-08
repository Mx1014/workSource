//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *<ul>
 * <li>billGroupId:账单组Id</li>
 * <li>billGroupName:账单组名称</li>
 * <li>billItemDTOList:账单组收费项目的集合，参考{@link com.everhomes.rest.asset.BillItemDTO}</li>
 * <li>exemptionItemDTOList:减免项集合，参考{@link com.everhomes.rest.asset.ExemptionItemDTO}</li>
 *</ul>
 */
public class BillGroupDTO {
    private String billGroupId;
    private String billGroupName;
    @ItemType(BillItemDTO.class)
    private List<BillItemDTO> billItemDTOList;
    @ItemType(ExemptionItemDTO.class)
    private List<ExemptionItemDTO> exemptionItemDTOList;

    public String getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(String billGroupId) {
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

    public List<ExemptionItemDTO> getExemptionItemDTOList() {
        return exemptionItemDTOList;
    }

    public void setExemptionItemDTOList(List<ExemptionItemDTO> exemptionItemDTOList) {
        this.exemptionItemDTOList = exemptionItemDTOList;
    }
}
