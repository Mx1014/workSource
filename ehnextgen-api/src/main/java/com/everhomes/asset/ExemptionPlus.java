package com.everhomes.asset;

/**
 * Created by Wentian on 2018/3/29.
 */
public enum ExemptionPlus {
    PLUS((byte)1), MINUS((byte)0);
    private byte code;
    ExemptionPlus(byte code){
        this.code = code;
    }
    public byte getCode() {
        return this.code;
    }

    public static ExemptionPlus fromCode(byte code){
        for(ExemptionPlus status : ExemptionPlus.values()){
            if(status.code == code){
                return status;
            }
        }
        return null;
    }

}
