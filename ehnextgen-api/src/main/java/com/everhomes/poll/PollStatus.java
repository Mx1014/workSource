package com.everhomes.poll;

public enum PollStatus {
    Invalid((byte) 0), Draft((byte) 1), Published((byte) 3);
    private Byte code;

    PollStatus(byte code) {
        this.code = code;
    }
    
    public Byte getCode(){
        return code;
    }

    public static PollStatus fromCode(byte code) {
        for (PollStatus s : PollStatus.values()) {
            if (s.code == code) {
                return s;
            }
        }
        return Invalid;
    }

    public static PollStatus fromStringCode(String code) {
        for (PollStatus s : PollStatus.values()) {
            if (s.name().equalsIgnoreCase(code)) {
                return s;
            }
        }
        return Invalid;
    }

}
