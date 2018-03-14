package com.everhomes.rest.asset;

/**
 * Created by Wentian on 2018/3/14.
 */
public enum BillSwitch {
    UNSETTLED((byte)0), SETTLED((byte)1), OTHERS((byte)3);
    private Byte code;
    BillSwitch(Byte code){
        this.code = code;
    }

    public Byte getCode(){
        return this.code;
    }
    public static BillSwitch fromCode(Byte code){
        if(code == null) return null;
        for(BillSwitch status: BillSwitch.values()){
            if(status.code.byteValue() == code){
                return status;
            }
        }
        return null;
    }
}
