package com.everhomes.poll;


public enum Selector {
    SINGLE_SELECT(0), MUTIL_SELECT(1);
    private Integer code;

    Selector(Integer code) {
        this.code = code;
    }

    public Byte getCode() {
        return Byte.valueOf((byte) this.code.intValue());
    }

    public static Selector fromCode(Byte code) {
        if(code==null){
            return SINGLE_SELECT;
        }
        for (Selector selector : Selector.values()) {
            if (selector.code == code.intValue()) {
                return selector;
            }
        }
        return SINGLE_SELECT;
    }
}
