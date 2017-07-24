package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>result: result</li>
 *     <li>body: body</li>
 * </ul>
 */
public class PayZuolinCreateWechatJsPayOrderResp {
	private boolean result;
	private CreateWechatJsPayOrderResp body;

	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public CreateWechatJsPayOrderResp getBody() {
		return body;
	}

	public void setBody(CreateWechatJsPayOrderResp body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
