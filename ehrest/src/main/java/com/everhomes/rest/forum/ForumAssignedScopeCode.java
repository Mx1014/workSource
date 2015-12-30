// @formatter:off
package com.everhomes.rest.forum;

/**
 * <p>热帖指定范围：</p>
 * <ul>
 * <li>ALL: 所有人</li>
 * <li>COMMUNITY: 小区</li>
 * <li>CITY: 城市</li>
 * </ul>
 *
 */
public enum ForumAssignedScopeCode {
    ALL((byte)0), COMMUNITY((byte)1), CITY((byte)2); 
    
    private byte code;
    
    private ForumAssignedScopeCode(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ForumAssignedScopeCode fromCode(Byte code) {
        if(code != null) {
            ForumAssignedScopeCode[] values = ForumAssignedScopeCode.values();
            for(ForumAssignedScopeCode value : values) {
                if(value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
