package com.everhomes.asset;

import com.everhomes.order.PaymentCallBackHandler;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.PreOrderCommand;
import com.everhomes.rest.order.PreOrderDTO;

public interface AssetPayServiceForEnt {
	
    public PreOrderDTO createPreOrder(PreOrderCommand cmd);
    
    public void payNotify(OrderPaymentNotificationCommand cmd, PaymentCallBackHandler handler);
    
}
