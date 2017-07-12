package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>RIGHT_OPEN: 1, 开门权限</li>
 * <li>RIGHT_VISITOR: 2, 访客授权权限</li>
 * <li>RIGHT_REMOTE: 3, 远程开门权限</li>
 * </ul>
 */
public enum DoorAuthRightType {
    RIGHT_OPEN((byte)1), RIGHT_VISITOR((byte)2), RIGHT_REMOTE((byte)3);

    private byte code;

    public byte getCode() {
        return this.code;
    }

    private DoorAuthRightType(byte code) {
        this.code = code;
    }
    
    public static DoorAuthRightType fromCode(Byte code) {
        if(code == null)
            return null;

        for (DoorAuthRightType doorAuthRightType: DoorAuthRightType.values()) {
            if(doorAuthRightType.code == code.byteValue()){
                return doorAuthRightType;
            }
        }
        return null;
    }
}
