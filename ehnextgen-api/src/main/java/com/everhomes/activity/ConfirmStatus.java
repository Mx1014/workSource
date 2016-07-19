package com.everhomes.activity;

public enum ConfirmStatus {
    UN_CONFIRMED((byte) 0), CONFIRMED((byte) 1);
    private Byte code;

    ConfirmStatus(byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static ConfirmStatus fromStringCode(String code) {
        for (ConfirmStatus flag : ConfirmStatus.values()) {
            if (flag.name().equalsIgnoreCase(code)) {
                return flag;
            }
        }
        return UN_CONFIRMED;
    }

    public static ConfirmStatus fromCode(Byte code) {
    	if(null == code){
    		return null;
    	}
        for (ConfirmStatus flag : ConfirmStatus.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}
