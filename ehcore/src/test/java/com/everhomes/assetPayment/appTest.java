//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.asset.ZhangJiangGaoKeThirdPartyAssetVendor;
import com.everhomes.rest.asset.ClientIdentityCommand;
import com.everhomes.rest.asset.ShowBillDetailForClientResponse;
import org.junit.Test;

/**
 * Created by Wentian Wang on 2017/9/10.
 */

public class appTest {


    @Test
    public void fun(){
         ZhangJiangGaoKeThirdPartyAssetVendor handler = new ZhangJiangGaoKeThirdPartyAssetVendor();
        ClientIdentityCommand command = new ClientIdentityCommand();
        command.setBillGroupId(null);
        command.setTargetId(311077l);
        command.setTargetType("eh_user");
        command.setOwnerId(null);
        command.setContractId("T1716170622");
        command.setOwnerType("community");
        command.setIsOnlyOwedBill((byte)0);
//        ShowBillForClientDTO showBillForClientDTO = handler.showBillForClient(command.getOwnerId(), command.getOwnerType(), command.getTargetType(), command.getTargetId(), command.getBillGroupId(), command.getIsOnlyOwedBill(), command.getContractId());
        ShowBillDetailForClientResponse eh_user = handler.getBillDetailForClient(null,"F9507BA7-9981-45A2-A75D-A4D39ADEDDDD", "eh_user");
        System.out.println(eh_user.getShowBillDetailForClientDTOList().get(0).getPayStatus());
//        System.out.println(showBillForClientDTO);
    }

}
