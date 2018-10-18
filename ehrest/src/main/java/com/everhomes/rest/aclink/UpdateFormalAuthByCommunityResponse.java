package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>errorCode:成功返回200, 错误可能类型:{@link com.everhomes.rest.aclink.AclinkServiceErrorCode}</li>
 * <li>msg:错误信息描述</li>
 * </ul>
 * 
 */
public class UpdateFormalAuthByCommunityResponse {
	private int errorCode;
	private String msg;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
