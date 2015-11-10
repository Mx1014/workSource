// @formatter:off
package com.everhomes.organization;

/**
 * <ul>
 * <li>1: 未处理</li>
 * <li>2: 处理中</li>
 * <li>3: 已处理</li>
 * <li>4: 其它</li>
 * </ul>
 */
public enum OrganizationTaskStatus {
	UNPROCESSED((byte)1), PROCESSING((byte)2), PROCESSED((byte)3), OTHER((byte)4);
    
    private byte code;
    private OrganizationTaskStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OrganizationTaskStatus fromCode(Byte code) {
        if(code != null) {
            OrganizationTaskStatus[] values = OrganizationTaskStatus.values();
            for(OrganizationTaskStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}