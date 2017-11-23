// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>HAVE_UNPAID_ORDER((byte)0): 用户存在未支付订单</li>
 * <li>LOGON_SUCCESS((byte)1) : 登录成功,可以立即打印</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintLogonStatusType {
	HAVE_UNPAID_ORDER((byte)0),LOGON_SUCCESS((byte)1);
	
	private byte code;

	private PrintLogonStatusType(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static PrintLogonStatusType fromCode(Byte code) {
		if(code == null)
			return null;
		for (PrintLogonStatusType t : PrintLogonStatusType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
