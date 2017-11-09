//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/10/17.
 */
/**
 *<ul>
 * <li>billGroupId:账单组id</li>
 *</ul>
 */
public class DeleteBillGroupCommand {
    private Long billGroupId;

    public Long getBillGroupId() {
        return billGroupId;
    }

    public void setBillGroupId(Long billGroupId) {
        this.billGroupId = billGroupId;
    }
}
