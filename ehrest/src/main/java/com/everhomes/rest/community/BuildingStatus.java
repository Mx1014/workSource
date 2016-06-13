package com.everhomes.rest.community;


/**
 * <ul>
 * <li>INACTIVE: 已删除</li>
 * <li>WAITING_FOR_CONFIRMATION: 待确认</li>
 * <li>ACTIVE: 正常</li>
 * </ul>
 */
public enum BuildingStatus {
	INACTIVE((byte)0), WAITING_FOR_CONFIRMATION((byte)1), ACTIVE((byte)2);
    
    private byte code;
    
    private BuildingStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static BuildingStatus fromCode(Byte code) {
        if(code != null) {
        	BuildingStatus[] values = BuildingStatus.values();
            for(BuildingStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
