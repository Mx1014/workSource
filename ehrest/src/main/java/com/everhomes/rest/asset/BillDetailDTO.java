//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>dateStr:账期</li>
 * <li>amountReceviable:应收金额</li>
 * <li>amountOwed:待缴金额</li>
 * <li>status:状态,1:待缴;2:缴清</li>
 * <li>billId:账单id</li>
 *</ul>
 */
public class BillDetailDTO {
    private String dateStr;
    private BigDecimal amountReceviable;
    private BigDecimal amountOwed;
    private Byte status;
    private Long billId;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public BigDecimal getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(BigDecimal amountOwed) {
        this.amountOwed = amountOwed;
    }

    public BigDecimal getAmountReceviable() {
        return amountReceviable;
    }

    public void setAmountReceviable(BigDecimal amountReceviable) {
        this.amountReceviable = amountReceviable;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public BillDetailDTO() {
    }
}
