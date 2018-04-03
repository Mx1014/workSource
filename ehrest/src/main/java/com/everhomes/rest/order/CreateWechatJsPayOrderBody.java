package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>body: CreateWechatJsPayOrderCmd 参考{@link com.everhomes.rest.order.CreateWechatJsPayOrderCmd}</li>
 * </ul>
 */
public class CreateWechatJsPayOrderBody {
	private CreateWechatJsPayOrderCmd body;

	public CreateWechatJsPayOrderCmd getBody() {
		return body;
	}

	public void setBody(CreateWechatJsPayOrderCmd body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
