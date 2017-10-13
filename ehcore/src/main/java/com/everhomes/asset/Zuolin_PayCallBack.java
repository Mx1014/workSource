//@formatter:off
package com.everhomes.asset;

import com.everhomes.db.DbProvider;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.SrvOrderPaymentNotificationCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wentian Wang on 2017/9/28.
 */
@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX+ OrderType.WUYE_CODE)
public class Zuolin_PayCallBack implements PaymentCallBackHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(Zuolin_PayCallBack.class);

    @Autowired
    private AssetProvider assetProvider;

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void paySuccess(SrvOrderPaymentNotificationCommand cmd) {
        Long orderId = cmd.getOrderId();
        AssetPaymentOrder order = assetProvider.findAssetPaymentById(orderId);
        List<AssetPaymentOrderBills> bills = assetProvider.findBillsById(orderId);
        Map<String,Integer> billStatuses = new HashMap<>();
        List<Long> billIds = new ArrayList<>();
        for(int i = 0; i < bills.size(); i++){
            AssetPaymentOrderBills bill = bills.get(i);
            //ORDER_BILL  中 1代表成功
            billStatuses.put(bills.get(i).getBillId(),1);
            billIds.add(Long.parseLong(bills.get(i).getBillId()));
        }
        //这个没有请求第三发，所以直接走
        Byte finalOrderStatus = 4;
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.changeOrderStaus(orderId, finalOrderStatus);
            assetProvider.changeBillStatusOnOrder(billStatuses,orderId);
            assetProvider.changeBillStatusOnPaiedOff(billIds);
            return null;
        });
    }

    @Override
    public void payFail(SrvOrderPaymentNotificationCommand cmd) {
        LOGGER.info("pay failed for zjgk, returned notificationCmd = {}",cmd.toString());
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.changeOrderStaus(cmd.getOrderId(),(byte)1);
            return null;
        });
    }

    @Override
    public void refundSuccess(SrvOrderPaymentNotificationCommand cmd) {
        LOGGER.info("pay failed for zjgk, returned notificationCmd = {}",cmd.toString());
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.changeOrderStaus(cmd.getOrderId(),(byte)6);
            return null;
        });
    }

    @Override
    public void refundFail(SrvOrderPaymentNotificationCommand cmd) {
        LOGGER.info("pay failed for zjgk, returned notificationCmd = {}",cmd.toString());
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.changeOrderStaus(cmd.getOrderId(),(byte)7);
            return null;
        });
    }
}
