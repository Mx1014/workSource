// @formatter:off
package com.everhomes.rest.news;

/**
 * 
 * <ul>
 * <li>DISABLED: 未开启</li>
 * <li>ENABLED: 开启</li>
 * </ul>
 */
public enum NewsNormalFlag {
	DISABLED((byte)0), ENABLED((byte)1);
	private byte code;
	private NewsNormalFlag(byte code) {
		this.code = code;
	}
	public byte getCode() {
        return this.code;
    }
	public static NewsNormalFlag fromCode(Byte code) {
    	if(code == null) {
    		return null;
    	}
        for(NewsNormalFlag flag : NewsNormalFlag.values()){
        	if(code.byteValue() == flag.getCode()){
        		return flag;
        	}
        }
        return null;
    }
}
