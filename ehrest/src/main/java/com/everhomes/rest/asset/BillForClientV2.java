//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/11/17.
 */

/**
 *<ul>
 * <li>billDuration:账期</li>
 * <li>amountReceivable:应缴金额</li>
 * <li>amountOwed:待缴金额</li>
 * <li>billId:账单ID</li>
 * <li>thirdPaid:1代表已经在第三方那边支付过，不允许在左邻再次支付；0：代表没在第三方支付过</li>
 *</ul>
 */
public class BillForClientV2 {
    private String billDuration;
    private String amountReceivable;
    private String amountOwed;
    private String billId;
    private Byte thirdPaid;

    public String getBillDuration() {
        return billDuration;
    }

    public void setBillDuration(String billDuration) {
        this.billDuration = billDuration;
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

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

	public Byte getThirdPaid() {
		return thirdPaid;
	}

	public void setThirdPaid(Byte thirdPaid) {
		this.thirdPaid = thirdPaid;
	}
}
