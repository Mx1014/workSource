package com.everhomes.rest.pmNotify;

/**
 * <ul>
 *     <li>0: 消息推送</li>
 *     <li>1: 短信提醒</li>
 * </ul>
 * Created by ying.xiong on 2017/9/11.
 */
public enum PmNotifyMode {
    MESSAGE((byte)0), SMS((byte)1);

    private byte code;

    private PmNotifyMode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PmNotifyMode fromCode(Byte code) {
        if(code != null) {
            PmNotifyMode[] values = PmNotifyMode.values();
            for(PmNotifyMode value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        return null;
    }
}
