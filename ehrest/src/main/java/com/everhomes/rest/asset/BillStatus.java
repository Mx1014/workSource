package com.everhomes.rest.asset;
/**
 * Created by Meng qianxiang on 2018/12/7.
 */
public enum BillStatus {
    UNFINISHED((byte)0), PAID_OFF((byte)1);
    private Byte code;
    BillStatus(Byte code){
        this.code = code;
    }

    public Byte getCode(){
        return this.code;
    }
    public static BillStatus fromCode(Byte code){
        if(code == null) return null;
        for(BillStatus status: BillStatus.values()){
            if(status.code.byteValue() == code){
                return status;
            }
        }
        return null;
    }
}
