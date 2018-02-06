//@formatter:off
package com.everhomes.warehouse;

/**
 * Created by Wentian Wang on 2018/2/6.
 */

public enum WarehouseStatus {
    SUSPEND((byte)1),INSTOCK((byte)2);

    private Byte code;
    WarehouseStatus(Byte code){
        this.code = code;
    }
    public byte getCode(){
        return this.code;
    }
    public static WarehouseStatus fromCode(Byte code){
        if(code == null){
            return null;
        }
        for(WarehouseStatus status : WarehouseStatus.values()){
            if(status.getCode() == code){
                return status;
            }
        }
        return null;
    }
}
