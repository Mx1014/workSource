package com.everhomes.payment.taotaogu;

public interface TaotaoguVendorConstant {
	//淘淘谷
	String TAOTAOGU = "TAOTAOGU";
	
	String KEY_STORE = "taotaogu.keystore";
	String PIN3_CRT = "taotaogu.pin3.crt";
	
	String CLIENT_PFX = "taotaogu.client.pfx";
	String SERVER_CER = "taotaogu.server.cer";
	
	String TAOTAOGU_APP_CARD_TRANSACTION_STATUS_JSON = "{\"CARD_TRANSACTION_STATUS_JSON\":{\"00\":\"处理中\",\"01\":\"处理成功\",\"02\":\"处理失败\",\"03\":\"已撤销\",\"04\":\"已冲正\",\"05\":\"已退货\",\"06\":\"已调账\",\"07\":\"已部分退货\"}}";
	String TAOTAOGU_CARD__STATUS_JSON = "{\"CARD_STATUS_JSON\":{\"00\":\"待制卡\",\"02\":\"制卡中\",\"03\":\"已入库\",\"04\":\"已领卡待售\",\"05\":\"预售出\",\"10\":\"正常（已激活）\",\"20\":\"已退卡(销户)\",\"21\":\"挂失\",\"22\":\"锁定\",\"23\":\"异常卡\"}}";
	String TAOTAOGU_WEB_CARD_TRANSACTION_STATUS_JSON = "{\"CARD_TRADE_STATUS_JSON\":{\"3\":\"支付成功\",\"4\":\"未支付\",\"0\":\"失败\",\"1\":\"过期\",\"2\":\"已撤销\"}}";

	
}
