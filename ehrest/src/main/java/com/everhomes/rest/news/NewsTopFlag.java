// @formatter:off
package com.everhomes.rest.news;

/**
 * 
 * <ul>
 * <li>NONE: 未置顶</li>
 * <li>TOP: 已置顶</li>
 * </ul>
 */
public enum NewsTopFlag {
	NONE((byte)0), TOP((byte)1);
	private byte code;
	private NewsTopFlag(byte code) {
		this.code = code;
	}
	public byte getCode() {
        return this.code;
    }
	public static NewsTopFlag fromCode(Byte code) {
    	if(code == null) {
    		return null;
    	}
        for(NewsTopFlag flag : NewsTopFlag.values()){
        	if(code.byteValue() == flag.getCode()){
        		return flag;
        	}
        }
        return null;
    }
}
