// @formatter:off
package com.everhomes.rest.general.order;

/**
 * 
 * <ul>
 * <li>PERSON_PAY((byte)0) : 个人支付</li>
 * <li>WAIT_FOR_ENTERPRISE_PAY((byte)1) : 已记账</li>
 * <li>ENTERPRISE_PAID((byte)2):已支付</li>
 * <li>ENTERPRISE_PAY((byte)3):企业支付（所有）</li>
 * </ul>
 *
 */
public enum GorderPayType {
    
	PERSON_PAY((byte)0,"个人支付"),WAIT_FOR_ENTERPRISE_PAY((byte)1,"已记账"),ENTERPRISE_PAID((byte)2,"已支付"),ENTERPRISE_PAY((byte)3,"企业支付");
	
	private byte code;
	private String desc;

	private GorderPayType(byte code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}

	public byte getCode() {
		return code;
	}

	public static GorderPayType fromCode(Byte code) {
		if(code == null)
			return null;
		for (GorderPayType t : GorderPayType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
