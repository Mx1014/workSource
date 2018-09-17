package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>NORMAL_PUNCH(0): 正常打卡创建 </li>
 * <li>AUTO_PUNCH(1): 自动打卡创建</li>
 * <li>FACE_PUNCH(2): 人脸识别打卡创建</li>
 * <li>DOOR_PUNCH(3): 门禁打卡创建</li>
 * <li>OTHER_THRID_PUNCH(4): 其他第三方接口创建(通过第三方接口打卡没有带创建类型)</li>
 * </ul>
 */
public enum CreateType {
    NORMAL_PUNCH((byte)0),AUTO_PUNCH((byte)1),FACE_PUNCH((byte)2),DOOR_PUNCH((byte)2),OTHER_THRID_PUNCH((byte)4);
    
    private byte code;
    private CreateType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static CreateType fromCode(Byte code) {
    	if(null == code)
    		return null;
        for(CreateType t : CreateType.values()) {
            if (t.code == code.byteValue()) {
                return t;
            }
        }
        return null;
    }
}
