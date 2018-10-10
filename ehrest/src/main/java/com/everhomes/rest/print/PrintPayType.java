// @formatter:off
package com.everhomes.rest.print;

/**
 * 
 * <ul>
 * <li>PERSON_PAY((byte)0) : 个人支付</li>
 * <li>WAIT_FOR_ENTERPRISE_PAY((byte)1) : 已记账</li>
 * <li>ENTERPRISE_PAID((byte)2):已支付</li>
 * </ul>
 *
 */
public enum PrintPayType {
    
	PERSON_PAY((byte)0,"个人支付"),WAIT_FOR_ENTERPRISE_PAY((byte)1,"记账"),ENTERPRISE_PAID((byte)2,"已支付");
	
	private byte code;
	private String desc;

	private PrintPayType(byte code, String desc){
		this.code = code;
		this.desc = desc;
	}
	
	public String getDesc(){
		return desc;
	}

	public byte getCode() {
		return code;
	}

	public static PrintPayType fromCode(Byte code) {
		if(code == null)
			return null;
		for (PrintPayType t : PrintPayType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
