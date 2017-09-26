// @formatter:off
package com.everhomes.rest.incubator;

/**
 * <ul>审批状态
 * <li>WAIT: 待审核</li>
 * <li>REJECT: 驳回</li>
 * <li>AGREE: 通过</li>
 * </ul>
 */
public enum ApproveStatus {
	WAIT((byte)0), REJECT((byte)1), AGREE((byte)2);

    private byte code;

    private ApproveStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ApproveStatus fromCode(Byte code) {
        if(code != null) {
            ApproveStatus[] values = ApproveStatus.values();
            for(ApproveStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
