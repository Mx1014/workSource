//@formatter:off
package com.everhomes.order;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PaymentCallBackCommand;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Wentian Wang on 2017/9/6.
 *
 * This is a "util" like service class for other businesses to call to make a payment. It is belongs to payment-2.0.
 *
 */


@Service
public class PayServiceImpl implements PayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private PayProvider payProvider;

    @Override
    public PreOrderCallBack createPreOrder(PreOrderMessage message) {
        PreOrderCallBack preOrderCallBack = new PreOrderCallBack();
        //查order表，如果order已经存在，则返回已有的合同，交易停止；否则，继续
        PaymentOrderRecord orderRecord = payProvider.findOrderRecordById(message.getOrderId());
        if(orderRecord!=null){
            preOrderCallBack = ConvertHelper.convert(orderRecord,PreOrderCallBack.class);
            preOrderCallBack.setAmount(message.getAmount());
            preOrderCallBack.setExtendInfo(message.getExtendInfo());
//            preOrderCallBack.setPayMethod(message.get);
        }
        //查account表，获取account信息
        //查收款方，如果收款方是会员，则continue；否则，交易不予进行
        //查付款方，如果付款方是会员，则继续；否则，如果付款方为个人，则创建一个会员，如果付款方为不为个人，则交易不予进行
        //组合报文发送给kelvin的支付系统，返回进行缓存，值为账单号
        //存储一份预订单record
        return null;
    }
    @Override
    public PaymentResult paymentNotify(PaymentMessage message) {
        return null;
    }

    @Override
    public void paymentCallBack(PaymentCallBackCommand cmd) {
        if(cmd.getOrderId()==null||cmd.getPaymentStatus()==null||cmd.getOrderType()==null){
            LOGGER.error("Invalid parameter,orderId,orderType or paymentStatus is null");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter,orderId,orderType or paymentStatus is null");
        }
        PaymentCallBackHandler handler = this.getOrderHandler(cmd.getOrderType());
        LOGGER.debug("PaymentCallBackHandler="+handler.getClass().getName());
        if(cmd.getPaymentStatus()==OrderPaymentStatus.SUCCESS.getCode()){
            handler.paySuccess(cmd);
        }
        if(cmd.getPaymentStatus()==OrderPaymentStatus.FAILED.getCode()){
            handler.payFail(cmd);
        }
    }

    private PaymentCallBackHandler getOrderHandler(String orderType) {
        return PlatformContext.getComponent(PaymentCallBackHandler.ORDER_PAYMENT_BACK_HANDLER_PREFIX+this.getOrderTypeCode(orderType));
    }

    private String getOrderTypeCode(String orderType) {
        Integer code = OrderType.OrderTypeEnum.getCodeByPyCode(orderType);
        if(code==null){
            LOGGER.error("Invalid parameter,orderType not found in OrderType.orderType="+orderType);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter,orderType not found in OrderType");
        }
        LOGGER.debug("orderTypeCode="+code);
        return String.valueOf(code);
    }
}
