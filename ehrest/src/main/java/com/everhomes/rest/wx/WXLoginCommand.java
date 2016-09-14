package com.everhomes.rest.wx;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>code: 微信登录令牌</li>
 * <li>state: 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击）</li> 
 * </ul>
 */
public class WXLoginCommand {
	private String code ;
	private String state;
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

}
