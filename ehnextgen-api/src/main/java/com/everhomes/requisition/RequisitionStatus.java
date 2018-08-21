package com.everhomes.requisition;

/**
 * Created by Wentian on 2018/2/5.
 */
public enum RequisitionStatus {
    WAIT((byte)0), HANDLING((byte)1), FINISH((byte)2), CANCELED((byte)3);
    private byte code;
    RequisitionStatus(byte code){
        this.code = code;
    }
    public byte getCode() {
        return this.code;
    }
    public static RequisitionStatus fromCode(Byte code){
        if(code == null) return null;
        for(RequisitionStatus status : RequisitionStatus.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }
}
