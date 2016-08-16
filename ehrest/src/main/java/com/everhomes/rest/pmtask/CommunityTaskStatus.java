// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>1: 未处理</li>
 * <li>2: 处理中</li>
 * <li>3: 已完成</li>
 * <li>4: 已关闭</li>
 * </ul>
 */
public enum CommunityTaskStatus {
	UNPROCESSED((byte)1), PROCESSING((byte)2), PROCESSED((byte)3), OTHER((byte)4);
    
    private byte code;
    private CommunityTaskStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static CommunityTaskStatus fromCode(Byte code) {
        if(code != null) {
            CommunityTaskStatus[] values = CommunityTaskStatus.values();
            for(CommunityTaskStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}