package com.everhomes.rest.asset;

/**
 * Created by Wentian on 2018/5/14.
 */
public enum BillsDayType {
    BEFORE_THIS_PERIOD((byte)1),
    AFTER_THIS_PERIOD((byte)2),
    END_THIS_PERIOD((byte)3),
    FIRST_MONTH_NEXT_PERIOD((byte)4),
    NEXT_PERIOD_FIRST_MONTH((byte)5);

    private Byte code;
    BillsDayType(byte code){
        this.code = code;
    }
    public Byte getCode(){return this.code;}
    public static BillsDayType fromCode(Byte code){
        if(code == null) return null;
        for(BillsDayType status : BillsDayType.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }
}
