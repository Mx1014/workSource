//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>billId:账单id</li>
 *</ul>
 */
public class ListBillDetailCommand {
    private Long billId;


    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }


    public ListBillDetailCommand() {

    }
}
