package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>DOOR_ACCESS((byte)0):单个门禁(门禁3.0之前的门禁组)</li>
 * <li>DOOR_GROUP((byte)1): 门禁组(门禁3.0)</li>
 * <li>COMMUNITY((byte)2):项目/园区</li>
 * <li>BUILDING((byte)3):楼栋</li>
 * <li>FLOOR((byte)4):楼层</li>
 * <li>ADDRESS((byte)5):门牌</li>
 * </ul>
 *
 */
public enum AuthGroupType {
    DOOR_ACCESS((byte) 0), DOOR_GROUP((byte) 1), COMMUNITY((byte) 2), BUILDING((byte) 3),
    FLOOR((byte) 4), ADDRESS((byte) 5);

    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private AuthGroupType(byte code) {
        this.code = code;
    }
    
    public static AuthGroupType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return DOOR_ACCESS;
        case 1 :
            return DOOR_GROUP;
        case 5:
            return COMMUNITY;
        case 6:
            return BUILDING;
        case 11:
            return FLOOR;
        case 12:
        	return ADDRESS;
        }
        
        return null;
    }
}
