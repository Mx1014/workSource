//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 *<ul>
 * <li>dateStr:账期</li>
 * <li>amountReceviable:应收金额</li>
 * <li>amountOwed:待缴金额</li>
 * <li>status:状态,0:待缴；1：已缴；2：欠费；3：未缴</li>
 * <li>billId:账单id</li>
 * <li>payStatus:清账状态</li>
 * <li>dateStrBegin:计费开始时间</li>
 * <li>dateStrEnd:计费结束时间</li>
 *</ul>
 */
public class BillDetailDTO {
    private String dateStr;
    private BigDecimal amountReceviable;
    private BigDecimal amountOwed;
    private Byte status;
    private String billId;
    private String payStatus;
    private String dateStrBegin;
    private String dateStrEnd;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getDateStr() {
        return dateStr;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
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

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public BillDetailDTO() {
    }

    public String getDateStrBegin() {
        return dateStrBegin;
    }

    public void setDateStrBegin(String dateStrBegin) {
        this.dateStrBegin = dateStrBegin;
    }

    public String getDateStrEnd() {
        return dateStrEnd;
    }

    public void setDateStrEnd(String dateStrEnd) {
        this.dateStrEnd = dateStrEnd;
    }
}
