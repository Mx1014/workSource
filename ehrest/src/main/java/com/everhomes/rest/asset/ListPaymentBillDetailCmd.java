//@formatter:off
package com.everhomes.rest.asset;

/**
 * @author created by ycx
 * @date 2018年11月20日 下午2:41:38
 */

/**
 *<ul>
 *<li>billId:账单id</li>
 *</ul>
*/
public class ListPaymentBillDetailCmd {
    //账单id
    private Long billId;

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}
    
}
