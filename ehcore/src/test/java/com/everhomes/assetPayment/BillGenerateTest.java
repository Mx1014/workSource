//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.asset.AssetService;
import com.everhomes.junit.CoreServerTestCase;
import com.everhomes.rest.asset.PaymentExpectanciesCommand;
import com.everhomes.rest.asset.PaymentExpectanciesResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Wentian Wang on 2017/8/23.
 */

public class BillGenerateTest extends CoreServerTestCase{
    @Autowired
    private AssetService assetService;
    @Test
    public void fun() {
        PaymentExpectanciesCommand cmd = new PaymentExpectanciesCommand();
//        cmd.setApartmentName();
//        cmd.setBuldingName();
//        cmd.setContractNum();
//        cmd.setFeesRules();
//        cmd.setNamesapceId();
//        cmd.setNoticeTel();
//        cmd.setOwnerId();
//        cmd.setOwnerType();
//        cmd.setPageOffset();
//        cmd.setPageSize();
//        cmd.setTargetId();
        cmd.setTargetName("");
        cmd.setTargetType("eh_user");
        PaymentExpectanciesResponse response = assetService.paymentExpectancies(cmd);
        System.out.println(response.getList());
    }

}
