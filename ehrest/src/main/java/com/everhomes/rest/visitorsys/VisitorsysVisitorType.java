// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * 
 * <ul>
 * <li>TEMPORARY((byte)0): 临时访客</li>
 * <li>BE_INVITED((byte)1): 预约访客</li>
 * </ul>
 */
public enum VisitorsysVisitorType {
	TEMPORARY((byte)0,"临时访客"),
	BE_INVITED((byte)1,"预约访客");
	
	private byte code;
	private String desc;

	private VisitorsysVisitorType(byte code,String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static VisitorsysVisitorType fromCode(Byte code) {
		if (code != null) {
			for (VisitorsysVisitorType status : VisitorsysVisitorType.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}

    public String getDesc() {
        return desc;
    }
}
