package com.everhomes.techpark.rental;
 
/**
 * <ul>
 * <li>LOCKED(0): 锁定，待支付 </li>
 * <li>RESERVED(1): 支付订金成功</li>
 * <li>SUCCESS(2): 全部支付预定成功</li>
 * <li>PAYINGFINAL(3): 待支付全款</li>
 * <li>FAIL(4): 失败，解锁</li>
 * <li>DELETED(5): 用户删除，不展示</li>
 * </ul>
 */
public enum SiteBillStatus {
   
    LOCKED((byte)0),RESERVED((byte)1),SUCCESS((byte)2),PAYINGFINAL((byte)3),FAIL((byte)4);
    
    private byte code;
    private SiteBillStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static SiteBillStatus fromCode(byte code) {
        for(SiteBillStatus t : SiteBillStatus.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
