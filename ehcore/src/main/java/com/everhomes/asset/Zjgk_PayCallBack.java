//@formatter:off
package com.everhomes.asset;

import com.everhomes.asset.zjgkVOs.NotifyPaymentResponse;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.http.HttpUtils;
import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.OrderType;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import java.util.*;

import static com.everhomes.util.SignatureHelper.computeSignature;

/**
 * Created by Wentian Wang on 2017/9/13.
 */
@Component(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX+ OrderType.ZJGK_RENTAL_CODE)
public class Zjgk_PayCallBack implements PaymentCallBackHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(Zjgk_PayCallBack.class);
    @Autowired
    private AssetProvider assetProvider;
    @Autowired
    private DbProvider dbProvider;

    /**
     * 请求张江高科的接口进行回调付款，成功后返回200和ok，则修改账单的状态
     */
    @Override
    public void paySuccess(com.everhomes.rest.order.OrderPaymentNotificationCommand cmd) {
        LOGGER.info("ZJGK-PAY-HANDLER START, cmd is = {}",cmd.toString());
        Map<String,String> params = null;
        Long orderId = cmd.getOrderId();
        LOGGER.info("ZJGK-PAY-HANDLER START, ORDER id = {}",orderId);
        AssetPaymentOrder order = assetProvider.findAssetPaymentById(cmd.getOrderId());
        List<AssetPaymentOrderBills> bills = assetProvider.findBillsById(orderId);
        Map<String,Integer> billStatuses = new HashMap<>();
        for(int i = 0; i < bills.size(); i++){
            params = new HashMap<String,String>();
            params.put("contractNum",order.getContractId());
            params.put("billId",bills.get(i).getBillId());
//            params.put("paidMoney",bills.get(i).getAmount().toString());
            params.put("paidMoney","102125");
            params.put("paidStatus","1");
            String json = generateJson(params);
            String url;
            if(order.getPayerType().equals("eh_organization")){
                url = ZjgkUrls.ENTERPRISE_BILLS_NOTIFY;
            }else if(order.getPayerType().equals("eh_user")){
                url = ZjgkUrls.USER_BILLS_NOTIFY;
            }else{
                LOGGER.error("payerType is incorrect,payerType = {}, billId = {}",order.getPayerType(),bills.get(i).getBillId());
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_ACCESS_DENIED,"payerType is incorrect,payerType = {}, billId = {}",order.getPayerType(),bills.get(i).getBillId());
            }
            String postJson;
            try {
                LOGGER.info("ready to request shenzhou for paymentnotify, json is = {}",json);
                postJson = HttpUtils.postJson(url, json, 120, HTTP.UTF_8);
                LOGGER.info("Get bill items from zjgk success, url={}, param={}, result={}", url, json, postJson);
            } catch (Exception e) {
                LOGGER.error("Failed to get bill item from zjgk, url={}, param={}", url, json, e);
                throw new RuntimeException("Request ShenZhouShuMa Failed"+e);
            }
            if(postJson==null || postJson.trim().length()<1){
                LOGGER.error("postJson is null or empty, postJson = {}",postJson);
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL,ErrorCodes.ERROR_ACCESS_DENIED,"postJson is null or empty, postJson = {}",postJson);
            }
            //记录哪些订单可以被改状态，persit需要改，由于有成功的，所以无法保证原子性；测试下对一个账单一把付款？
            NotifyPaymentResponse response = (NotifyPaymentResponse)StringHelper.fromJsonString(postJson, NotifyPaymentResponse.class);
            if(response.getErrorCode()==200){
            //此订单付款成功。统一订单状态：0：新建；1：支付失败；2：支付成功但张江高科的全部失败；3：支付成功但张江高科的部分成功；4：支付成功张江高科的也全部成功;5：取消;6:退款成功；7：退款失败
                // 各个账单的状态：0:没有支付；1：支付成功；2：支付部分成功
                billStatuses.put(bills.get(i).getBillId(),1);
            }else{
                billStatuses.put(bills.get(i).getBillId(),0);
            }
        }
        Byte orderStatus = null;
        //支付成功后统一订单的状态判断
        if(billStatuses.containsValue(0)&&billStatuses.containsValue(1)){
            //partly success
            orderStatus = 3;
        }else if (! billStatuses.containsValue(1)){
            // all failed
            orderStatus = 2;
        }else if(! billStatuses.containsValue(0)){
            //all success
            orderStatus = 4;
        }
        Byte finalOrderStatus = orderStatus;
        LOGGER.info("FINAL orderStatus is = {}",finalOrderStatus);
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.changeOrderStaus(orderId, finalOrderStatus);
            assetProvider.changeBillStatusOnOrder(billStatuses,orderId);
            return null;
        });
    }

    /**
     * 修改账单状态为fail
     */
    @Override
    public void payFail(com.everhomes.rest.order.OrderPaymentNotificationCommand cmd) {
        LOGGER.info("pay failed for zjgk, returned notificationCmd = {}",cmd.toString());
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.changeOrderStaus(cmd.getOrderId(),(byte)1);
            return null;
        });
        // order的状态给为fail
    }

    @Override
    public void refundSuccess(com.everhomes.rest.order.OrderPaymentNotificationCommand cmd) {
        LOGGER.info("pay failed for zjgk, returned notificationCmd = {}",cmd.toString());
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.changeOrderStaus(cmd.getOrderId(),(byte)6);
            return null;
        });
    }

    @Override
    public void refundFail(com.everhomes.rest.order.OrderPaymentNotificationCommand cmd) {
        LOGGER.info("pay failed for zjgk, returned notificationCmd = {}",cmd.toString());
        this.dbProvider.execute((TransactionStatus status) -> {
            assetProvider.changeOrderStaus(cmd.getOrderId(),(byte)7);
            return null;
        });
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
