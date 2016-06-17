package com.everhomes.rest.news;

/**
 * 
 * <ul>
 * <li>UNTOPPED: 未置顶</li>
 * <li>TOPPED: 已置顶</li>
 * </ul>
 */
public enum NewsTopFlag {
	UNTOPPED((byte)0),TOPPED((byte)1);
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
