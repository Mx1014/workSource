//@formatter:off
package com.everhomes.rest.asset;
/**
 * @author created by ycx
 * @date 下午1:46:26
 */

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 *</ul>
 */
public class ShowCreateBillSubItemListCmd {
    private Long billGroupId;

	public Long getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}
}
