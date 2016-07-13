// @formatter:off
package com.everhomes.rest.forum;

/**
 * <ul>帖子是否为官方帖
 * <li>NO(0): 不是</li>
 * <li>YES(1): 是</li>
 * </ul>
 */
public enum OfficialFlag {

    NO((byte)0), YES((byte)1);
    
    private byte code;
    
    private OfficialFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OfficialFlag fromCode(Byte code) {
        if(code != null){
        	for (OfficialFlag flag : OfficialFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
        }
        
        return null;
    }
}
