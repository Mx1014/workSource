package com.everhomes.order;

import com.everhomes.rest.order.RefundCallbackCommand;

/**
 * <p>订单退款回调处理类<p>
 * <ul>
 * 	<li>1.创建新类，实现本接口{@link com.everhomes.order.RefundEmbeddedHandler}</li>
 * 	<li>2.在新类头部添加注解，如：{@code @Component(OrderEmbeddedHandler.ORDER_EMBEDED_OBJ_RESOLVER_PREFIX+OrderType.WU_YE_TEST_CODE)}</li>
 * 	<li>3.实现{@code void paySuccess(PayCallbackCommand cmd)}和{@code void payFail(PayCallbackCommand cmd)}方法</li>
 * </ul> 
 */
public interface RefundEmbeddedHandler {
	String ORDER_EMBEDED_OBJ_RESOLVER_PREFIX = "OrderEmbededApp-";
	/**
	 * 支付成功
	 */
	void paySuccess(RefundCallbackCommand cmd);
	/**
	 * 支付失败
	 */
	void payFail(RefundCallbackCommand cmd);

}
