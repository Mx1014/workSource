//@formatter:off
package com.everhomes.purchase;

import com.everhomes.requisition.RequisitionStatus;

/**
 * Created by Wentian Wang on 2018/2/6.
 */

public enum PurchaseOrderStatus {
    HANDLING((byte)1), FINISH((byte)2), CANCELED((byte)3);
    private byte code;
    PurchaseOrderStatus(byte code){
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
