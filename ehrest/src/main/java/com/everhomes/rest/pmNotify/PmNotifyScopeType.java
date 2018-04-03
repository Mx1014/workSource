package com.everhomes.rest.pmNotify;

/**
 * <ul>
 *     <li>0: all</li>
 *     <li>1: namespace</li>
 *     <li>2: community</li>
 * </ul>
 * Created by ying.xiong on 2017/9/12.
 */
public enum PmNotifyScopeType {
    ALL((byte)0), NAMESPACE((byte)1), COMMUNITY((byte)2);

    private byte code;

    private PmNotifyScopeType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PmNotifyScopeType fromCode(Byte code) {
        if(code != null) {
            PmNotifyScopeType[] values = PmNotifyScopeType.values();
            for(PmNotifyScopeType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
