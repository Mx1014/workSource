package com.everhomes.statistics.transaction;

import java.util.Arrays;
import java.util.List;

public interface StatTransactionConstant {

	String BIZ_SERVER_URL = "stat.biz.server.url";
	
	String PAID_SERVER_URL = "stat.paid.server.url";
	
	String BIZ_PAID_ORDER_API = "get.paid.order.api";
	
	String BIZ_REFUND_ORDER_API = "get.refund.order.api";
	
	String BIZ_WARE_INFO_API = "get.ware.info.api";

	String BIZ_BUSINESSES_INFO_API = "get.businesses.info.api";
	
	String PAID_TRANSACTION_API = "get.transaction.api";
	
	String PAID_REFUND_API = "get.refund.api";
	
	String BIZ_APPKEY = "stat.biz.appkey";
	
	String BIZ_SECRET_KEY = "stat.biz.secretkey";
	
	String PAID_APPKEY = "stat.paid.appkey";
	
	String PAID_SECRET_KEY = "stat.paid.secretkey";
	
	String STAT_CRON_EXPRESSION = "statistics.cron.expression";
	
	String PAY_RECORD_PAY_TYPE_ALIPAY = "alipay";
	
	String PAY_RECORD_PAY_TYPE_WECHAT = "wechat";
	
	String PAY_RECORD_ORDER_TYPE_DIANSHANG = "dianshang";
	
	String PAY_RECORD_ORDER_TYPE_WUYE = "wuye";
	
	String PAY_RECORD_ORDER_TYPE_TINGCHE = "tingche";
	
	String PAY_RECORD_ORDER_TYPE_HUIYISHI = "huiyishiorder";
	
	String PAY_RECORD_ORDER_TYPE_VIPCHEWEI = "vipcheweiorder";
	
	String PAY_RECORD_ORDER_TYPE_DIANZIPING = "dianzipingorder";
	
	String PAY_RECORD_ORDER_TYPE_PAYMENTCARD = "paymentCard";
	
	String PAY_RECORD_ORDER_TYPE_RENTALORDER = "rentalOrder";
	
	String PAY_RECORD_ORDER_TYPE_VIDEOCONF = "videoConf";
	
	String PAY_RECORD_ORDER_TYPE_PARKING = "parking";
	
	String PAY_RECORD_ORDER_TYPE_PMSY = "pmsy";
	
	List<String> COMMUNITY_SERVICES = Arrays.asList(
			PAY_RECORD_ORDER_TYPE_WUYE,
			PAY_RECORD_ORDER_TYPE_TINGCHE,
			PAY_RECORD_ORDER_TYPE_HUIYISHI,
			PAY_RECORD_ORDER_TYPE_VIPCHEWEI,
			PAY_RECORD_ORDER_TYPE_DIANZIPING,
			PAY_RECORD_ORDER_TYPE_PAYMENTCARD,
			PAY_RECORD_ORDER_TYPE_RENTALORDER,
			PAY_RECORD_ORDER_TYPE_VIDEOCONF,
			PAY_RECORD_ORDER_TYPE_PARKING,
			PAY_RECORD_ORDER_TYPE_PMSY
	);
	
	byte PAY_ORDER_SHOP_TYPE_PLATFORM = 1;
	
	byte PAY_ORDER_SHOP_TYPE_SELF = 2;
}
