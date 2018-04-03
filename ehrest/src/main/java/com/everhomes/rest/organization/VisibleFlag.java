// @formatter:off
package com.everhomes.rest.organization;

/**
 * <ul>是否隐藏
 * <li>SHOW(0): 显示</li>
 * <li>HIDE(1): 隐藏</li>
 * </ul>
 */
public enum VisibleFlag {

    SHOW((byte)0), HIDE((byte)1), ALL((byte)3);

    private byte code;

    private VisibleFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static VisibleFlag fromCode(Byte code) {
        if(code != null){
        	for (VisibleFlag flag : VisibleFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
        }
        
        return null;
    }
}
