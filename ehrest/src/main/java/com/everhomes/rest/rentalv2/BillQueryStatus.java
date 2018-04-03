package com.everhomes.rest.rentalv2;
 
/**
 * <ul>
 * <li>VALID(0):有效订单 </li>
 * <li>CANCELED(1): 已取消订单</li>
 * <li>FINISHED(2): 已结束订单 </li>
 * <li>UNPAY(3): 待支付 </li>
 * </ul>
 */
public enum BillQueryStatus {
   
	
	VALID((byte)0),
	CANCELED((byte)1),
	FINISHED((byte)2),
	UNPAY((byte)3);
	
    
    private byte code;
    private BillQueryStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static BillQueryStatus fromCode(byte code) {
        for(BillQueryStatus t : BillQueryStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
