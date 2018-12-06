// @formatter:off
package com.everhomes.rest.officecubicle;

/**
 * <ul>工位预定订单状态 
 * <li>UNPAID(1): 待付款</li>
 * <li>PAID(2): 待使用</li> 
 * <li>IN_USE(3): 使用中</li>
 * <li>COMPLETE(4): 已完成</li>
 * <li>REFUNDING(5): 退款中</li>
 * <li>REFUNDED(6): 已退款</li>
 * </ul>
 */
public enum OfficeCubiceOrderStatus {
    /*INACTIVE((byte)0, "无效"), */UNPAID((byte)1, "待付款"), PAID((byte)2, "待使用"), IN_USE((byte)3, "使用中"),
    COMPLETE((byte)4, "已完成"), REFUNDING((byte)5, "退款中"), REFUNDED((byte)6, "已退款"),EFFECTIVE((byte)11,"有效订单");
    
    private byte code;
    private String description;
    
    private OfficeCubiceOrderStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OfficeCubiceOrderStatus fromCode(Byte code) {
        if(code != null) {
        	OfficeCubiceOrderStatus[] values = OfficeCubiceOrderStatus.values();
            for(OfficeCubiceOrderStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }

    public String getDescription() {
        return description;
    }
}
