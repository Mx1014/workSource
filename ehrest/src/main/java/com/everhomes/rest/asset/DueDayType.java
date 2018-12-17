package com.everhomes.rest.asset;

public enum DueDayType {
    DAY_TYPE((byte)1),
    MONTH_TYPE((byte)2),
    DATE_TYPE((byte)3);

    private Byte code;
    DueDayType(byte code){
        this.code = code;
    }
    public Byte getCode(){return this.code;}
    public static DueDayType fromCode(Byte code){
        if(code == null) return null;
        for(DueDayType status : DueDayType.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }
}
