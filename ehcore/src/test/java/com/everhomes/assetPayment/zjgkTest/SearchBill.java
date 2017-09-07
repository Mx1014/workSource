//@formatter:off
package com.everhomes.assetPayment.zjgkTest;

import com.everhomes.asset.ZhangjianggaokeAssetVendor;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.asset.ListBillsDTO;
import com.everhomes.rest.asset.ListBillsResponse;
import org.junit.Test;

import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/5.
 */

public class SearchBill{
    @Test
    public void fun(){
        ZhangjianggaokeAssetVendor v = new ZhangjianggaokeAssetVendor();
        Integer namespaceId = 999971;
        Long ownerId = 240111044331050388l;
        Integer pageSize = 20;
        ListBillsResponse response = new ListBillsResponse();
        List<ListBillsDTO> dtos = v.listBills("", "", namespaceId, ownerId, "", "", "", null, null, null
                , (byte) 0, "", "", 1, pageSize, null, null, "eh_user", response);
        System.out.println(dtos.size());
        System.out.println("--------------------------");
        System.out.println(dtos);
    }
}
