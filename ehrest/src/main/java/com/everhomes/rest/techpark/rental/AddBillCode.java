package com.everhomes.rest.techpark.rental;
 
/**
 * <ul>
 * <li>NORMAL(0): 正常 </li>
 * <li>CONFLICT(1):冲突：该时间段有订单 </li> 
 * </ul>
 */
public enum AddBillCode {
   
    NORMAL((byte)0),CONFLICT((byte)1);
    
    private byte code;
    private AddBillCode(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AddBillCode fromCode(byte code) {
        for(AddBillCode t : AddBillCode.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
