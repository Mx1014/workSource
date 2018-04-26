package com.everhomes.rest.warehouse;

/**
 * Created by Wentian on 2018/3/9.
 */
public enum OrderServiceType {
    NORMAL_ENTRY((byte)1),REQUEST_OUTSTOCK((byte)2),PURCHASE_ENTRY((byte)3);
    private Byte code;
    OrderServiceType(Byte code){
        this.code = code;
    }
    public Byte getCode() {
        return code;
    }
    public static OrderServiceType fromCode(Byte status){
        if(status == null) return null;
        for(OrderServiceType serviceType : OrderServiceType.values()){
            if(serviceType.getCode().byteValue() == status.byteValue()) return serviceType;
        }
        return null;
    }
}
