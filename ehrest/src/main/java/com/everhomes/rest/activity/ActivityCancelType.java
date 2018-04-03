package com.everhomes.rest.activity;
/**
 * <ul>
 * 	<li>HAND: 0, 手动取消</li>
 *  <li>EXPIRE_AUTO: 1，过期自动取消</li>
 *  <li>PAY_FAIL: 2，支付失败</li>
 * </ul>
 */
public enum ActivityCancelType {
    HAND((byte) 0), EXPIRE_AUTO((byte) 1), PAY_FAIL((byte)2);
    private Byte code;

    ActivityCancelType(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ActivityCancelType fromStringCode(String code) {
        for (ActivityCancelType flag : ActivityCancelType.values()) {
            if (flag.name().equalsIgnoreCase(code)) {
                return flag;
            }
        }
        return HAND;
    }

    public static ActivityCancelType fromCode(Byte code) {
    	if(null == code){
    		return null;
    	}
        for (ActivityCancelType flag : ActivityCancelType.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
