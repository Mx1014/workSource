//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/21.
 */

import java.util.Date;

/**
 *<ul>
 * <li>billGroupName:账单组名称</li>
 * <li>dateStrBegin：计费开始日期</li>
 * <li>dateStrEnd：计费结束日期</li>
 * <li>amountReceivable：应收</li>
 * <li>amountOwed：欠收</li>
 * <li>chargeStatus：0：未付款；1：已付款</li>
 * <li>confirmFlag：支付状态是否已确认字段，1：已确认；0：待确认</li>
 *</ul>
 */
public class ListAllBillsForClientDTO {
    private String billGroupName;
    private String dateStrBegin;
    private String dateStrEnd;
    private String amountReceivable;
    private String amountOwed;
    private Byte chargeStatus;
    private String dateStr;
    private Date date;
    private Byte confirmFlag;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private String billId;

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillGroupName() {
        return billGroupName;
    }

    public void setBillGroupName(String billGroupName) {
        this.billGroupName = billGroupName;
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

    public String getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(String amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public String getAmountOwed() {
        return amountOwed;
    }

    public void setAmountOwed(String amountOwed) {
        this.amountOwed = amountOwed;
    }

    public Byte getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(Byte chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

	public Byte getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(Byte confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

}
