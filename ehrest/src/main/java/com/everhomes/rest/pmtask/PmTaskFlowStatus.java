// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>0: 无效任务</li>
 * <li>1: 待受理</li>
 * <li>2: 待分配</li>
 * <li>3: 处理中</li>
 * <li>4: 已完成</li>
 * </ul>
 */
public enum PmTaskFlowStatus {
	INACTIVE((byte)0), ACCEPTING((byte)1), ASSIGNING((byte)2), PROCESSING((byte)3), COMPLETED((byte)4);
    
    private byte code;
    private PmTaskFlowStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskFlowStatus fromCode(Byte code) {
        if(code != null) {
            PmTaskFlowStatus[] values = PmTaskFlowStatus.values();
            for(PmTaskFlowStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}