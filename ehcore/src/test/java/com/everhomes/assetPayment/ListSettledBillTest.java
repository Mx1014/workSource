//@formatter:off
package com.everhomes.assetPayment;


import com.everhomes.asset.AssetService;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.asset.ListSettledBillCommand;
import com.everhomes.rest.asset.ListSettledBillItemsCommand;
import com.everhomes.rest.asset.ListSettledBillItemsResponse;
import com.everhomes.rest.asset.ListSettledBillResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ListSettledBillTest extends CoreServerTestCase {
    @Autowired
    private AssetService assetService;

    @Test
    public void fun(){
        ListSettledBillCommand cmd = new ListSettledBillCommand();
        cmd.setOwnerId(240111044331055036l);
        cmd.setOwnerType("community");
        cmd.setPageAnchor(null);
        cmd.setPageSize(3);
        cmd.setDateStrBegin(null);
        cmd.setDateStrEnd(null);
        ListSettledBillResponse listSettledBillResponse = assetService.listSettledBill(cmd);

    }
    @Test
    public void fun1(){
        ListSettledBillCommand cmd = new ListSettledBillCommand();
        cmd.setOwnerId(240111044331055036l);
        cmd.setOwnerType("community");
//        cmd.setPageAnchor(null);
//        cmd.setPageSize(3);
//        cmd.setDateStrBegin(null);
//        cmd.setDateStrEnd(null);
//        cmd.setBillGroupName("物业");
//        cmd.setTargetName("lisi");
        cmd.setAddressName("beijing");
        ListSettledBillResponse listSettledBillResponse = assetService.listSettledBill(cmd);
        for(int i = 0; i<1000; i++){
            System.out.println(i);
        }
        System.out.println(listSettledBillResponse);
    }
    @Test
    public void fun2(){
        ListSettledBillItemsCommand cmd = new ListSettledBillItemsCommand();
        cmd.setOwnerId(240111044331055036l);
        cmd.setOwnerType("community");
//        cmd.setPageAnchor(null);
//        cmd.setPageSize(3);
//        cmd.setDateStrBegin(null);
//        cmd.setDateStrEnd(null);
//        cmd.setBillGroupName("物业");
//        cmd.setTargetName("lisi");
        cmd.setBillId(1l);
        ListSettledBillItemsResponse response = assetService.listSettledBillItems(cmd);
        for(int i = 0; i<1000; i++){
            System.out.println(i);
        }
        System.out.println(response);
    }
}
