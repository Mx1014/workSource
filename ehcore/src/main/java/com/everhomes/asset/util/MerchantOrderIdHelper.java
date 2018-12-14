package com.everhomes.asset.util;

import com.everhomes.asset.AssetProvider;
import com.everhomes.asset.PaymentBillItems;
import com.everhomes.rest.asset.PaymentBillsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MerchantOrderIdHelper {

    @Autowired
    private AssetProvider assetProvider;

    public  Set<String> getAllMerchantOrderIdByBillId(PaymentBillsCommand cmd){
        Set<String> MerchantOrderIds = null;
       List<PaymentBillItems> billItems = assetProvider.findPaymentBillItems(cmd.getNamespaceId(),
               cmd.getTargetId(),cmd.getTargetType(),cmd.getBillId());
       if (billItems!=null&&billItems.size()>0){
           MerchantOrderIds = new HashSet<>();
           for (PaymentBillItems billItem:billItems){
               MerchantOrderIds.add(billItem.getMerchantOrderId());
           }
       }
        return MerchantOrderIds;
    }
}
