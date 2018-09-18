package com.everhomes.rest.rentalv2;
 
/**
 * <ul>
 *     注意：INACTIVE(12)，APPROVING(13)，PAYINGFINAL(3)，FAIL(4)，REFUNDING(9)，REFUNDED(10)
 *     都属于单元格没有被锁定的状态，不会消耗资源数量
 *
 * <li>SUCCESS(2): 已预约,待使用</li>
 * <li>PAYINGFINAL(3): 待支付全款</li>
 * <li>FAIL(4): 已取消</li>
 * <li>FAIL_PAID(5): 已取消(已付款) 只做展示用</li>
 * <li>COMPLETE(7): 完成使用</li>
 * <li>OVERTIME(8): 超时未完成(已预约成功，但是使用完没有手动完成，超时)</li>
 * <li>REFUNDING(9): 退款中</li>
 * <li>REFUNDED(10): 已退款</li>
 * <li>INACTIVE(12): 无效订单(点击下一步产生的订单，还没有预约)</li>
 * <li>APPROVING(13): 待审批</li>
 * <li>OWING_FEE(20): 欠费</li>
 * </ul>
 */
public enum SiteBillStatus {

	SUCCESS((byte)2,"已预约"),
	PAYINGFINAL((byte)3,"待付款"),
	FAIL((byte)4,"已取消"),
    FAIL_PAID((byte)5,"已取消(已支付)"),

	COMPLETE((byte)7,"已完成"),
	OVERTIME((byte)8,"已过期"),

	REFUNDING((byte)9,"退款中"),
	REFUNDED((byte)10,"已退款"),

/*    @Deprecated
    OFFLINE_PAY((byte)11,"线下支付"),*/

    INACTIVE((byte)12, "无效订单"),
    APPROVING((byte)13, "待审批"),

    IN_USING((byte)14, "使用中"),
    OWING_FEE((byte)20, "欠费");
    
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
