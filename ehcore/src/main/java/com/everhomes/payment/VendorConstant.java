package com.everhomes.payment;

public interface VendorConstant {
	//淘淘谷
	String TAOTAOGU = "TAOTAOGU";
	
	String INITIAL_PASSWORD = "init_password";

	String BRANCH_CODE = "BranchCode";
	String APP_NAME = "AppName";
	String VERSION = "Version";
	String DSTID = "DstId";
	
	String KEY_STORE = "taotaogu.keystore";
	String PIN3_CRT = "taotaogu.pin3.crt";
	
	String CLIENT_PFX = "taotaogu.client.pfx";
	String SERVER_CER = "taotaogu.server.cer";
	
	String TAOTAOGU_AESKEY = "TAOTAOGU_AESKEY";
	String TAOTAOGU_TOKEN = "TAOTAOGU_TOKEN";
	
	//渠道类型 chnl_type
	String CHNL_TYPE = "chnl_type";
	//渠道号 CHNL_ID
	String CHNL_ID = "chnl_id";
	//商户号 MERCH_ID
	String MERCH_ID = "merch_id";
	//终端号 TERMNL_ID
	String TERMNL_ID = "termnl_id";
	
	String CARD_TRANSACTION_STATUS_JSON = "{\"CARD_TRANSACTION_STATUS_JSON\":{\"00\":\"处理中\",\"01\":\"处理成功\",\"02\":\"处理失败\",\"03\":\"已撤销\",\"04\":\"已冲正\",\"05\":\"已退货\",\"06\":\"已调账\",\"07\":\"已部分退货\"}}";
	String CARD__STATUS_JSON = "{\"CARD_STATUS_JSON\":{\"00\":\"待制卡\",\"02\":\"制卡中\",\"03\":\"已入库\",\"04\":\"已领卡待售\",\"05\":\"预售出\",\"10\":\"正常（已激活）\",\"20\":\"已退卡(销户)\",\"21\":\"挂失\",\"22\":\"锁定\",\"23\":\"异常卡\"}}";
	String CARD_TRADE_STATUS_JSON = "{\"CARD_TRADE_STATUS_JSON\":{\"00\":\"支付成功\",\"01\":\"未支付\",\"02\":\"失败\",\"03\":\"过期\",\"04\":\"已撤销\"}}";

	
}
