package com.everhomes.rest.comment;

/**
 * <ul>园区app类型
 * <li>FORUM(1)</li>
 * <li>NEWS(2)</li>
 * <li>SERVICEALLIANCE(3)</li>
 * </ul>
 */
public enum OwnerType {

	FORUM((byte)1),NEWS((byte)2),SERVICEALLIANCE((byte)3);

    private byte code;

    private OwnerType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OwnerType fromCode(Byte code) {
        if(code != null) {
        	OwnerType[] values = OwnerType.values();
            for(OwnerType value : values) {
                if(code.byteValue() == value.getCode()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
