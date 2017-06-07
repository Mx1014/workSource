package com.everhomes.rest.rentalv2;
 
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
 * <li>OFFLINE_PAY(11): 线下支付</li>
 * <li>INACTIVE(12): 无效订单(点击下一步产生的订单，还没有预约)</li>
 * <li>APPROVING(13): 待审批)</li>
 * </ul>
 */
public enum SiteBillStatus {
   
	@Deprecated
	LOCKED((byte)0,"待付订金"),
    @Deprecated
	RESERVED((byte)1,"已付定金"),

	SUCCESS((byte)2,"已预约"),
	PAYINGFINAL((byte)3,"待付款"),
	FAIL((byte)4,"已取消"),
//	TOPAYRES((byte)5),
//	TOPAYFINAL((byte)6),
	COMPLETE((byte)7,"已完成"),
	OVERTIME((byte)8,"已过期"),
	REFUNDING((byte)9,"退款中"),
	REFUNDED((byte)10,"已退款"),

    @Deprecated
    OFFLINE_PAY((byte)11,"线下支付"),

    INACTIVE((byte)12, "无效订单"),
    APPROVING((byte)13, "待审批");
    
    private byte code;
	private String describe;
    private SiteBillStatus(byte code,String describe) {
        this.code = code;
        this.describe = describe;
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

	public String getDescribe() {
		return describe;
	}
 
}
