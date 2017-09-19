//@formatter:off
package com.everhomes.asset;

import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.everhomes.util.SignatureHelper.computeSignature;

/**
 * Created by Wentian Wang on 2017/9/13.
 */
@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX+ "zjgkrentalcode")
public class Zjgk_PayCallBack implements PaymentCallBackHandler{
    @Autowired
    private AssetProvider assetProvider;

    /**
     * 请求张江高科的接口进行回调付款，成功后返回200和ok，则修改账单的状态
     */
    @Override
    public void paySuccess(com.everhomes.rest.order.OrderPaymentNotificationCommand cmd) {
        Map<String,String> params = null;
        Long orderId = cmd.getOrderId();
        AssetPaymentOrder order = assetProvider.findAssetPaymentById(cmd.getOrderId());
        List<AssetPaymentOrderBills> bills = assetProvider.findBillsById(orderId);
        List<String> billIds = new ArrayList<>();
        for(int i = 0; i < bills.size(); i++){
            AssetPaymentOrderBills bill = bills.get(i);
            String billId = bill.getBillId();
            billIds.add(billId);
        }
        for(int i = 0; i < billIds.size(); i++){
            String billId = billIds.get(i);
            params = new HashMap<String,String>();
            params.put("contractNum","tt");
            params.put("billId",billId);
            params.put("paidMoney","1000")
        }
//        params.put();
    }

    /**
     * 修改账单状态为fail
     */
    @Override
    public void payFail(com.everhomes.rest.order.OrderPaymentNotificationCommand cmd) {
        // order的状态给为fail
    }
}
