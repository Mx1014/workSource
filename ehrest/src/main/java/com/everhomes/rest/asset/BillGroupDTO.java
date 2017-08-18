//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;

import java.util.List;

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>billItemDTOList:账单组收费项目的集合，参考{@link com.everhomes.rest.asset.BillItemDTO}</li>
 * <li>exemptionItemDTOList:减免项集合，参考{@link com.everhomes.rest.asset.ExemptionItemDTO}</li>
 *</ul>
 */
public class BillGroupDTO {
    private Long billGroupId;
    @ItemType(BillItemDTO.class)
    private List<BillItemDTO> billItemDTOList;
    @ItemType(ExemptionItemDTO.class)
    private List<ExemptionItemDTO> exemptionItemDTOList;

    public List<BillItemDTO> getBillItemDTOList() {
        return billItemDTOList;
    }

    public void setBillItemDTOList(List<BillItemDTO> billItemDTOList) {
        this.billItemDTOList = billItemDTOList;
    }

    public List<ExemptionItemDTO> getExemptionItemDTOList() {
        return exemptionItemDTOList;
    }

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }

    public void setExemptionItemDTOList(List<ExemptionItemDTO> exemptionItemDTOList) {
        this.exemptionItemDTOList = exemptionItemDTOList;
    }
}
