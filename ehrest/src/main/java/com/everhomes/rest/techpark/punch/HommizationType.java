package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>人性化设置:0-无 1-弹性 2晚到晚走</li> 
 * </ul>
 */
public enum HommizationType {

    NO((byte) 0), FLEX((byte) 1), LATEARRIVE((byte) 2);

    private byte code;

    HommizationType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static HommizationType fromCode(Byte code) {
        if (code == null) {
            return null;
        }
        for (HommizationType t : HommizationType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        return null;
    }
}
