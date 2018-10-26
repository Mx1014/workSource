package com.everhomes.rest.pusher;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>code:标志码：1：成功；其他：失败</li>
 * <li>msg:响应内容</li>
 * </ul>
 * 
 * @author moubinmo
 *
 */

public class ThirdPartResponseMessage {
	private Integer code;
	private String msg;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
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
