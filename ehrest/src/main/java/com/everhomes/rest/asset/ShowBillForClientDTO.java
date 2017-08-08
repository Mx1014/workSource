//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 *<ul>
 * <li>billPeriodMonths:待缴月数</li>
 * <li>amountOwed:欠收金额</li>
 * <li>billDetailDTOList:账单列表，参考{@link BillDetailDTO}</li>
 *</ul>
 */
public class ShowBillForClientDTO {
    private Integer billPeriodMonths;
    private BigDecimal amountOwed;
    @ItemType(BillDetailDTO.class)
    List<BillDetailDTO> billDetailDTOList;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getBillPeriodMonths() {
        return billPeriodMonths;
    }

    public void setBillPeriodMonths(Integer billPeriodMonths) {
        this.billPeriodMonths = billPeriodMonths;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public List<BillDetailDTO> getBillDetailDTOList() {
        return billDetailDTOList;
    }

    public void setBillDetailDTOList(List<BillDetailDTO> billDetailDTOList) {
        this.billDetailDTOList = billDetailDTOList;
    }

    public ShowBillForClientDTO() {

    }
}
