//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.asset.bill.ListBillsResponse;

import org.junit.Test;

/**
 * Created by Wentian Wang on 2017/9/10.
 */

public class GetStuffs extends CoreServerTestCase{
//    @Autowired
//    ZzhangjianggaokeAssetVendor zhangjianggaokeAssetVendor;
    @Test
    public void fun(){
        ListBillsResponse response = new ListBillsResponse();
        Byte billStatus = null;
        Long ownerId = 8728341232l;
        Byte status = 1;
//        zhangjianggaokeAssetVendor.listBills(
//                "",999971, ownerId, "community", "","", 1l, "", 1l, billStatus, "", "", 1, 1000000, "",status,"eh_organization", response
//        );
    }

}
