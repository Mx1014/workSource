package com.everhomes.purchase;

/**
 * Created by Wentian on 2018/2/6.
 */
public enum PurchaseSubmissionStatus {
    UNINITIALIZED((byte)0),HANDLING((byte)1),FINISH((byte)2),CANCELED((byte)3);
    private byte code;
    PurchaseSubmissionStatus(byte code) {
        this.code = code;
    }
    public byte getCode(){
        return this.code;
    }
    public static PurchaseSubmissionStatus fromCode(Byte code){
        if(code == null){
            return null;
        }
        for(PurchaseSubmissionStatus status : PurchaseSubmissionStatus.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }
}
