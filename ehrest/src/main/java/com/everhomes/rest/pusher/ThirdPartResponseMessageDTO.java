package com.everhomes.rest.pusher;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>code:标志码：1：成功；其他：失败</li>
 * <li>msg:响应内容</li>
 * <li>extra:附加内容：若有发送失败的用户手机号，则返回全部失败手机号，','分割；否则为""</li>
 * </ul>
 * 
 * @author moubinmo
 *
 */

public class ThirdPartResponseMessageDTO {
	private Integer code;
	private String msg;
	private String extra;
	
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
	
    public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
