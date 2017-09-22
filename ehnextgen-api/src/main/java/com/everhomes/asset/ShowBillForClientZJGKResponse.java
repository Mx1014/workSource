//@formatter:off
package com.everhomes.asset;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/8/15.
 */

public class ShowBillForClientZJGKResponse {
    private List<BillDTO_zj> billDTOS;

    public List<BillDTO_zj> getBillDTOS() {
        return billDTOS;
    }

    public void setBillDTOS(List<BillDTO_zj> billDTOS) {
        this.billDTOS = billDTOS;
    }
}
