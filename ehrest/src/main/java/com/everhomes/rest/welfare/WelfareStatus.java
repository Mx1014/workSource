package com.everhomes.rest.welfare;
/**
 * 
 * <ul>福利状态:
 * <li>DRAFT((byte) 0): 草稿</li>
 * <li>SENDING((byte) 1): 发送中</li>
 * <li>SENDED((byte) 2): 已发送</li>
 * <li>FAILED((byte) 3): 发送失败</li>
 * </ul>
 */
public enum WelfareStatus {
    DRAFT((byte) 0),SENDING((byte) 1),SENDED((byte) 2),FAILED((byte) 3);
    private byte code;

    private WelfareStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static WelfareStatus fromCode(Byte code) {
        WelfareStatus[] values = WelfareStatus.values();
        if(null == code)
        	return null;
        for (WelfareStatus value : values) {
            if (code.equals(value.code)) {
                return value;
            }
        }

        return null;
    }
}
