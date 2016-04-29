package com.everhomes.organization.pmsy;

import org.springframework.stereotype.Component;

import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.PM_SIYUAN_CODE )
public class PmsyOrderEmbeddedHandler implements OrderEmbeddedHandler{

	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		// TODO Auto-generated method stub
		
	}

}
