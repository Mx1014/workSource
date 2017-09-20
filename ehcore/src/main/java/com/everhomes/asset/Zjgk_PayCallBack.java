//@formatter:off
package com.everhomes.asset;

import com.everhomes.constants.ErrorCodes;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(Zjgk_PayCallBack.class);
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
        for(int i = 0; i < bills.size(); i++){
            params = new HashMap<String,String>();
            params.put("contractNum",order.getContractId());
            params.put("billId",bills.get(i).getBillId());
            params.put("paidMoney",bills.get(i).getAmount().toString());
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

        }
        //总的状态修改
//        params.put();
    }

    /**
     * 修改账单状态为fail
     */
    @Override
    public void payFail(com.everhomes.rest.order.OrderPaymentNotificationCommand cmd) {
        
        // order的状态给为fail
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
