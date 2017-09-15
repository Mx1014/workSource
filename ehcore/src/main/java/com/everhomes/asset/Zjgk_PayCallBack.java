//@formatter:off
package com.everhomes.asset;

import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.everhomes.util.SignatureHelper.computeSignature;

/**
 * Created by Wentian Wang on 2017/9/13.
 */
@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX+ "zjgkrentalcode")
public class Zjgk_PayCallBack implements PaymentCallBackHandler{
    @Autowired
    private AssetProvider assetProvider;
    @Override
    public void paySuccess(OrderPaymentNotificationCommand cmd) {
        Map<String,String> params = null;
        Long orderId = cmd.getOrderId();
//        AssetPaymentOrder order = assetProvider.findOrderById(orderId);
        findBillsOnOrderId
        String billIds = order.getBillIds();
        String[] split = billIds.split(",");
        for(int i = 0; i < split.length; i++){
            String billId = split[i];
            params = new HashMap<String,String>();
            params.put("contractNum","tt");
            params.put("billId",billId);
            params.put("paidMoney","1000")
        }
        params.put();
    }

    @Override
    public void payFail(OrderPaymentNotificationCommand cmd) {
        //failed
        //调用张江高科的支付接口
    }
    private String generateJson(Map<String,String> params){
        params.put("appKey", "ee4c8905-9aa4-4d45-973c-ede4cbb3cf21");
        params.put("nonce", "54256");
        params.put("timestamp", "1498097655000");
        params.put("crypto", "sssss");
        String SECRET_KEY = "2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==";
        params.put("signature",computeSignature(params,SECRET_KEY));
        return StringHelper.toJsonString(params);
    }
}
