// @formatter:off
package com.everhomes.express;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.order.OrderEmbeddedHandler;
import com.everhomes.rest.order.OrderType;
import com.everhomes.rest.order.PayCallbackCommand;

@Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX + OrderType.EXPRESS_ORDER_CODE )
public class ExpressOrderEmbeddedHandler implements OrderEmbeddedHandler {

	@Autowired
	private ExpressService expressService;
	
	@Override
	public void paySuccess(PayCallbackCommand cmd) {
		expressService.paySuccess(cmd);
	}

	@Override
	public void payFail(PayCallbackCommand cmd) {
		expressService.payFail(cmd);
	}
	
}
