// @formatter:off
package com.everhomes.rest.print;
/**
 * 
 * <ul>
 * <li>PRINT((byte)1) : 打印</li>
 * <li>COPY((byte)2) : 复印</li>
 * <li>SCAN((byte)3) : 扫描</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintJobTypeType {
	PRINT((byte)1,"PRINT","打印"),COPY((byte)2,"COPY","复印"),SCAN((byte)3,"SCAN","扫描");
	
	private byte code;
	private String desc;
	private String describe;

	private PrintJobTypeType(byte code,String desc, String describe){
		this.code = code;
		this.desc = desc;
		this.describe = describe;
	}
	
	public String getDescribe() {
		return describe;
	}

	public byte getCode() {
		return code;
	}
	
	public String getDesc(){
		return this.desc;
	}

	public static PrintJobTypeType fromCode(Byte code) {
		if(code == null)
			return null;
		for (PrintJobTypeType t : PrintJobTypeType.values()) {
			if (t.code == code) {
				return t;
			}
		}

		return null;
	}
}
