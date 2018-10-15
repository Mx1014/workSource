package com.everhomes.general.order;

import com.everhomes.general.order.DefaultGeneralOrderHandler;
import com.everhomes.general.order.GeneralOrderBizHandler;
import com.everhomes.rentalv2.RentalOrder;
import com.everhomes.rentalv2.Rentalv2AccountProvider;
import com.everhomes.rentalv2.Rentalv2OrderRecord;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.AssetSourceType;
import com.everhomes.rest.general.order.CreateOrderBaseInfo;
import com.everhomes.rest.general.order.OrderCallBackCommand;
import com.everhomes.rest.order.OrderType.OrderTypeEnum;
import com.everhomes.rest.promotion.order.CreateGeneralBillInfo;
import com.everhomes.rest.rentalv2.PayChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = GeneralOrderBizHandler.GENERAL_ORDER_HANDLER + "rentalOrder")
public class RentalGeneralOrderHandlerImpl extends DefaultGeneralOrderHandler {

    @Autowired
    private Rentalv2AccountProvider rentalv2AccountProvider;
    @Autowired
    private Rentalv2Provider rentalProvider;
    @Override
    OrderTypeEnum getOrderTypeEnum() {
        return OrderTypeEnum.RENTALORDER;
    }

    @Override
    void fillEnterprisePaySpecificInfo(CreateGeneralBillInfo billInfo, CreateOrderBaseInfo baseInfo) {
    }

    @Override
    void dealInvoiceCallBack(OrderCallBackCommand cmd) {
        RentalOrder rentalOrder = this.getRentalOrderByMerchantOrderId(Long.valueOf(cmd.getCallBackInfo().getBusinessOrderId()));
        rentalOrder.setInvoiceFlag(TrueOrFalseFlag.TRUE.getCode());
        rentalProvider.updateRentalBill(rentalOrder);
    }

    @Override
    void dealEnterprisePayCallBack(OrderCallBackCommand cmd) {
        RentalOrder rentalOrder = this.getRentalOrderByMerchantOrderId(Long.valueOf(cmd.getCallBackInfo().getBusinessOrderId()));
        rentalOrder.setPayChannel(PayChannel.ENTERPRISE_PAY_COMPLETE.getCode());
        rentalProvider.updateRentalBill(rentalOrder);
    }

    private RentalOrder getRentalOrderByMerchantOrderId(Long id){
        Rentalv2OrderRecord orderRecord = rentalv2AccountProvider.getOrderRecordByMerchantOrderId(id);
        if (orderRecord != null)
            return rentalProvider.findRentalBillByOrderNo(orderRecord.getOrderNo().toString());
        return null;
    }
}
