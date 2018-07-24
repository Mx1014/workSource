//@formatter:off
package com.everhomes.rest.asset;

import com.everhomes.discover.ItemType;

/**
 * Created by Wentian Wang on 2018/4/16.
 */
/**
 *<ul>
 * <li>billId:账单id</li>
 * <li>invoiceNum:发票编号</li>
 * <li>noticeTel:催缴联系号码</li>
 *</ul>
 */
public class ModifySettledBillCommand {
    private Long billId;
    private String invoiceNum;
    private String noticeTel;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

	public String getNoticeTel() {
		return noticeTel;
	}

	public void setNoticeTel(String noticeTel) {
		this.noticeTel = noticeTel;
	}
}
