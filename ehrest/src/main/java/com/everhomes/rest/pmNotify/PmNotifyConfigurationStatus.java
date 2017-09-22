package com.everhomes.rest.pmNotify;

/**
 * <ul>
 *     <li>0: 失效</li>
 *     <li>1: 有效</li>
 * </ul>
 * Created by ying.xiong on 2017/9/12.
 */
public enum PmNotifyConfigurationStatus {
    INVAILD((byte)0), VAILD((byte)1);

    private byte code;

    private PmNotifyConfigurationStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PmNotifyConfigurationStatus fromCode(Byte code) {
        if(code != null) {
            PmNotifyConfigurationStatus[] values = PmNotifyConfigurationStatus.values();
            for(PmNotifyConfigurationStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
