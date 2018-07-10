//@formatter:off
package com.everhomes.rest.organization.pm;


/**
 * Created by Wentian Wang on 2018/6/12.
 */

public enum  ReservationStatus {
    INACTIVE((byte)1), ACTIVE((byte)2), DELTED((byte)3);
    private Byte code;
    ReservationStatus (Byte code){
        this.code = code;
    }

    public Byte getCode(){
        return this.code;
    }

    public static ReservationStatus fromCode(Byte code){
        if(code == null) return null;
        for(ReservationStatus status : ReservationStatus.values()){
            if(status.code.byteValue() == code.byteValue()){
                return status;
            }
        }
        return null;
    }
}
