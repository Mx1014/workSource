// @formatter:off
package com.everhomes.rest.ui.forum;

/**
 * <ul>是否显示图片
 * <li>NO(0): 不是</li>
 * <li>YES(1): 是</li>
 * </ul>
 */
public enum MediaDisplayFlag {
	NO((byte)0), YES((byte)1);
    
    private byte code;
    
    private MediaDisplayFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static MediaDisplayFlag fromCode(Byte code) {
        if(code != null){
        	for (MediaDisplayFlag flag : MediaDisplayFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
        }
        
        return null;
    }
}
