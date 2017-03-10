package com.everhomes.activity;

public enum CheckInStatus {
    CHECKIN((byte) 1, "是"), UN_CHECKIN((byte) 0, "否");
    private byte code;
    private String text;

    CheckInStatus(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public Byte getCode() {
        return code;
    }

    public static CheckInStatus fromCode(Byte code) {
    	
    	if(null == code){
    		return null;
    	}
        for (CheckInStatus flag : CheckInStatus.values()) {
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
