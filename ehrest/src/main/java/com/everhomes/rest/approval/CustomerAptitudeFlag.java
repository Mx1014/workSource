package com.everhomes.rest.approval;

public enum CustomerAptitudeFlag {
    NOAPTITUDE((byte) 0), APTITUDE((byte) 1);

    private byte code;

    private CustomerAptitudeFlag(byte code) {
        this.code = code;
    }

    public byte getCode(){
        return this.code;
    }

    public static CustomerAptitudeFlag fromCode(Byte code){
        if (code != null) {
            for (CustomerAptitudeFlag flag : CustomerAptitudeFlag.values()) {
                if (code.byteValue() == flag.getCode()) {
                    return flag;
                }
            }
        }

        return null;
    }
}
