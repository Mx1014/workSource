package com.everhomes.rest.xfyun;

import com.everhomes.util.StringHelper;

public class AfterDealCommand {
    private String signature;       //加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、rand参数。
    
    private String timestamp;       //时间戳
    
    private String  rand;            //随机数
    
    private String data;
    
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getRand() {
		return rand;
	}

	public void setRand(String rand) {
		this.rand = rand;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
    
}
