// @formatter:off
package com.everhomes.express;

import java.util.Map;

import com.everhomes.rest.express.GetExpressLogisticsDetailResponse;

public interface ExpressHandler {
	
	String EXPRESS_HANDLER_PREFIX = "express_handler_";

	String getBillNo(ExpressOrder expressOrder);

	GetExpressLogisticsDetailResponse getExpressLogisticsDetail(ExpressCompany expressCompany, String billNo);
	
	/**
	 *  国贸项目直接创建订单，不获取订单号
	 */
	void createOrder(ExpressOrder expressOrder, ExpressCompany expressCompany);
	
	/**
	 *  国贸项目更新订单
	 */
	void updateOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany);
	
	void getOrderStatus(ExpressOrder expressOrder, ExpressCompany expressCompany);
	
	/**
	 * 国贸项目EMS快递状态回调
	 */
	void orderStatusCallback(ExpressOrder expressOrder, ExpressCompany expressCompany, Map<String,String> params);
	
}
