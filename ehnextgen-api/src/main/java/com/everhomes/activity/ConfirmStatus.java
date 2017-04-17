package com.everhomes.activity;

public enum ConfirmStatus {
    UN_CONFIRMED((byte) 0, "待确认"), CONFIRMED((byte) 1, "已确认"), REJECT((byte) 2, "已驳回");
    private Byte code;
    private String text;

    ConfirmStatus(byte code, String text) {
        this.code = code;
        this.text = text;
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

	public String getText() {
		return text;
	}
}
