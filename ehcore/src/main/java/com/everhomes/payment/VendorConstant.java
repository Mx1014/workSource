package com.everhomes.payment;

public interface VendorConstant {
	//淘淘谷
	String TAOTAOGU = "TAOTAOGU";
	
	String INITIAL_PASSWORD = "111111";

	
	String APP_NAME = "ICCard";
	String VERSION = "V0.01";
	String DSTID = "00000000";
	
	String KEY_STORE = "taotaogu.keystore";
	String PIN3_CRT = "taotaogu.pin3.crt";
	
	String CLIENT_PFX = "taotaogu.client.pfx";
	String SERVER_CER = "taotaogu.server.cer";
	
	String TAOTAOGU_AESKEY = "TAOTAOGU_AESKEY";
	String TAOTAOGU_TOKEN = "TAOTAOGU_TOKEN";
	
	//渠道类型 chnl_type
	String CHNL_TYPE = "WEB";
	//渠道号 CHNL_ID
	String CHNL_ID = "12345679";
	//商户号 MERCH_ID
	String MERCH_ID = "862900000000001";
	//终端号 TERMNL_ID
	String TERMNL_ID = "00011071";
	
	String CARD_TRANSACTION_STATUS_JSON = "{\"CARD_TRANSACTION_STATUS_JSON\":{\"00\":\"处理中\",\"01\":\"成功\",\"02\":\"失败\",\"03\":\"已撤销\",\"04\":\"已冲正\",\"05\":\"已退货\",\"06\":\"已调账\",\"07\":\"已部分退货\"}}";
	String CARD__STATUS_JSON = "{\"CARD_STATUS_JSON\":{\"00\":\"待制卡\",\"02\":\"制卡中\",\"03\":\"已入库\",\"04\":\"已领卡待售\",\"05\":\"预售出\",\"10\":\"正常（已激活）\",\"20\":\"已退卡(销户)\",\"21\":\"挂失\",\"22\":\"锁定\",\"23\":\"异常卡\"}}";

	
}
