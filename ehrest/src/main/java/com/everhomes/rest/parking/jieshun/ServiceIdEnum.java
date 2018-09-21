package com.everhomes.rest.parking.jieshun;

import com.everhomes.util.StringHelper;

/**
 * @author 黄明波
 *
 */
public enum ServiceIdEnum {

	QUERY_CARD_INFO("3c.base.querypersonsbycar"), //根据车牌号获取月卡&人信息
	QUERY_CARD_TYPES_AND_FEES("3c.park.queryparkstandard"),//获取停车场的缴费信息
	NOTIFY_MONTHLIY_CARD_RECHARGE("3c.card.delaybycar"),//月卡充值
	QUERY_TEMP_FEE_ORDER("3c.pay.createorderbycarno"),//根据车牌号获取临时缴费订单
	NOTIFY_TEMP_FEE_RECHARGE("3c.pay.notifyorderresult"),//临时车缴费充值
	NONE(""); //只是用于代码结尾

	private String code;

	public String getCode() {
		return code;
	}

	private ServiceIdEnum(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}

/*
QUERY_CARD_INFO("3c.base.querypersonsbycar"), //根据车牌号获取月卡&人信息
request:{"p":"{\"requestType\":\"DATA\",\"attributes\":{\"areaCode\":\"0010028888\",\"carNo\":\"粤-LLL000\"},\"serviceId\":\"3c.base.querypersonsbycar\"}","v":"2","tn":"e7f5279355844872b2d840e6f23834cf1536575015520","sn":"5771E0873E22F5AE7C07D7BE13AFB5E7","cid":"880075500000001"}
response:{"attributes":{},"dataItems":[{"attributes":{"birthday":"","sex":"MALE","personCode":"SZ000032","secretKey":"45412455","identityCode":"11343","email":"","telephone":"13488861339","personName":"zt2","areaId":"5cdd45c47ff0477c92eee59edb565502"},"failItems":[],"objectId":"PERSON","operateType":"READ","subItems":[{"attributes":{"cardId":"7f84d762f7a94416ab549611cb767160","carNo":"粤-LLL000","issueTime":"2018-09-05 14:40:25","package":"{\"3\":3.00,\"1\":1.00,\"6\":6.00,\"12\":12.00}","endDate":"2018-10-04","physicalNo":"粤LLL000","beginDate":"2018-09-05","endTime":"2018-10-04","cardType":"纯车牌月卡A"},"failItems":[],"objectId":"CARD","operateType":"READ","subItems":[]}]}],"message":"成功","resultCode":0,"seqId":"-93nux2_1k2","serviceId":"3c.base.querypersonsbycar"}
QUERY_CARD_TYPES_AND_FEES("3c.park.queryparkstandard"),//获取停车场的缴费信息
NOTIFY_MONTHLIY_CARD_RECHARGE("3c.card.delaybycar"),//月卡充值
QUERY_TEMP_FEE_ORDER("3c.pay.createorderbycarno"),//根据车牌号获取临时缴费订单
NOTIFY_TEMP_FEE_RECHARGE("3c.pay.notifyorderresult"),//临时车缴费充值




*/
