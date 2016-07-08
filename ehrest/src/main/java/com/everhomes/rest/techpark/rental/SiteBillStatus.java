package com.everhomes.rest.techpark.rental;
 
/**
 * <ul>
 * <li>LOCKED(0): 锁定，待支付 </li>
 * <li>RESERVED(1): 支付订金成功</li>
 * <li>SUCCESS(2): 全部支付预定成功</li>
 * <li>PAYINGFINAL(3): 待支付全款</li>
 * <li>FAIL(4): 失败，解锁</li>
 * <li>TOPAYRES(5):进入支付定金</li>
 * <li>TOPAYFINAL(6): 进入支付全款</li>
 * <li>COMPLETE(7): 完成预约</li>
 * <li>OVERTIME(8): 超时未完成</li>
 * <li>REFUNDING(9): 退款中</li>
 * <li>REFUNDED(10): 已退款</li>
 * </ul>
 */
public enum SiteBillStatus {
   
	
	LOCKED((byte)0),
	RESERVED((byte)1),
	SUCCESS((byte)2),
	PAYINGFINAL((byte)3),
	FAIL((byte)4),
	TOPAYRES((byte)5),
	TOPAYFINAL((byte)6),
	COMPLETE((byte)7),
	OVERTIME((byte)8),
	REFUNDING((byte)9),
	REFUNDED((byte)10);
	
    
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
