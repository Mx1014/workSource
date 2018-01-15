package com.everhomes.rest.customer;

/**
 * <ul>
 * <li>CREATED(1): 已创建未执行</li>
 * <li>EXECUTING(2): 执行中</li>
 * <li>FINISH(3): 正常完成</li>
 * <li>EXCEPTION(4): 异常完成</li>
 * </ul>
 * Created by ying.xiong on 2018/1/13.
 */
public enum SyncDataTaskStatus {
    CREATED((byte)1), EXECUTING((byte)2), FINISH((byte)3), EXCEPTION((byte)4);

    private byte code;
    private SyncDataTaskStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static SyncDataTaskStatus fromCode(Byte code) {
        if(code != null) {
            for(SyncDataTaskStatus value : SyncDataTaskStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
