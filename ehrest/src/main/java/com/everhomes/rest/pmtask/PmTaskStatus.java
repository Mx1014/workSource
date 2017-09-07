// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>0: 已取消</li>
 * <li>1: 待处理</li>
 * <li>2: 处理中（分配人员）</li>
 * <li>3: 已完成</li>
 * <li>4: 已关闭</li>
 * <li>5: 已回访</li>
 * </ul>
 */
public enum PmTaskStatus {
	INACTIVE((byte)6), UNPROCESSED((byte)1), PROCESSING((byte)2), PROCESSED((byte)3), CLOSED((byte)4), REVISITED((byte)5);
    
    private byte code;
    private PmTaskStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PmTaskStatus fromCode(Byte code) {
        if(code != null) {
            PmTaskStatus[] values = PmTaskStatus.values();
            for(PmTaskStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}