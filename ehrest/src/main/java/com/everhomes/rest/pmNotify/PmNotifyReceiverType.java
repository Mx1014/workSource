package com.everhomes.rest.pmNotify;

/**
 * <ul>
 *     <li>0: 执行人</li>
 *     <li>1: 审批人</li>
 *     <li>2: 部门、分公司等组织架构树中的某一层级</li>
 *     <li>3: 特定的人</li>
 * </ul>
 * Created by ying.xiong on 2017/9/12.
 */
public enum PmNotifyReceiverType {
    EXECUTOR((byte)0), REVIEWER((byte)1), ORGANIZATION((byte)2), USER((byte)3);

    private byte code;

    private PmNotifyReceiverType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static PmNotifyReceiverType fromCode(Byte code) {
        if(code != null) {
            PmNotifyReceiverType[] values = PmNotifyReceiverType.values();
            for(PmNotifyReceiverType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }
}
