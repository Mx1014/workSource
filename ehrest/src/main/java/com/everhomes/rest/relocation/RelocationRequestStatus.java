// @formatter:off
package com.everhomes.rest.relocation;

/**
 * <ul>物品搬迁申请状态
 * <li>CANCELED(0): 已取消</li>
 * <li>COMPLETED(1): 已完成</li>
 * <li>PROCESSING(2): 处理中</li>
 * </ul>
 */
public enum RelocationRequestStatus {
	CANCELED((byte)0,"已取消"), COMPLETED((byte)1,"已完成"), PROCESSING((byte)2,"处理中");

    private byte code;
    private String description;

    private RelocationRequestStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public byte getCode() {
        return this.code;
    }

    public String getDescription() {
        return description;
    }

    public static RelocationRequestStatus fromCode(Byte code) {
        if(code != null) {
            RelocationRequestStatus[] values = RelocationRequestStatus.values();
            for(RelocationRequestStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
