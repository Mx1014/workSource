package com.everhomes.purchase;

/**
 * Created by Wentian on 2018/2/6.
 */
/**
 *<ul>
 * <li>UNINITIALIZED:0,未发起</li>
 * <li>HANDLING:1，处理中</li>
 * <li>FINISH:2，已完成</li>
 * <li>CANCELED:3，已取消</li>
 *</ul>
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
