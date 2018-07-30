//@formatter:off
package com.everhomes.rest.asset;
/**
 * @author created by ycx
 * @date 下午1:46:26
 */

/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 * <li>categoryId:多应用入口区分标识</li>
 *</ul>
 */
public class ShowCreateBillSubItemListCmd {
    private Long billGroupId;
    private Long categoryId;

	public Long getBillGroupId() {
		return billGroupId;
	}

	public void setBillGroupId(Long billGroupId) {
		this.billGroupId = billGroupId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
}
