//@formatter:off
package com.everhomes.assetPayment;


import com.everhomes.asset.AssetService;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.asset.*;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ListSettledBillTest extends CoreServerTestCase {
    @Autowired
    private AssetService assetService;

    @Test
    public void fun(){
        ListBillsCommand cmd = new ListBillsCommand();
        cmd.setOwnerId(240111044331055036l);
        cmd.setOwnerType("community");
        cmd.setPageAnchor(null);
        cmd.setPageSize(3);
        cmd.setDateStrBegin(null);
        cmd.setDateStrEnd(null);
        ListBillsResponse listBillsResponse = assetService.listBills(cmd);

    }
    @Test
    public void fun1(){
        ListBillsCommand cmd = new ListBillsCommand();
        cmd.setOwnerId(240111044331055036l);
        cmd.setOwnerType("community");
//        cmd.setPageAnchor(null);
//        cmd.setPageSize(3);
//        cmd.setDateStrBegin(null);
//        cmd.setDateStrEnd(null);
//        cmd.setBillGroupName("物业");
//        cmd.setTargetName("lisi");
//        cmd.setAddressName("beijing");
        ListBillsResponse listBillsResponse = assetService.listBills(cmd);
        for(int i = 0; i<1000; i++){
            System.out.println(i);
        }
        System.out.println(listBillsResponse);
    }
    @Test
    public void fun2(){
        ListBillItemsCommand cmd = new ListBillItemsCommand();
        cmd.setOwnerId(240111044331055036l);
        cmd.setOwnerType("community");
//        cmd.setPageAnchor(null);
//        cmd.setPageSize(3);
//        cmd.setDateStrBegin(null);
//        cmd.setDateStrEnd(null);
//        cmd.setBillGroupName("物业");
//        cmd.setTargetName("lisi");
//        cmd.setBillId(1l);

        ClientIdentityCommand c = new ClientIdentityCommand();
        c.setOwnerType("community");
        c.setOwnerId(240111044331055036l);
        c.setTargetType("eh_user");
        c.setTargetId(43324234234l);
        c.setBillGroupId(1l);
        User u = new User();
        u.setId(240278l);
        UserContext.current().setUser(u);
        ListBillItemsResponse response = assetService.listBillItems(cmd);
        ShowBillForClientDTO showBillForClientDTO = assetService.showBillForClient(c);
        for(int i = 0; i<1000; i++){
            System.out.println(i);
        }
        System.out.println(showBillForClientDTO);
    }
}
