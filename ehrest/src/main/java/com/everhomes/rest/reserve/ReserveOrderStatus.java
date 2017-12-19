// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>订单状态
 * <li>INACTIVE(0): 无效</li>
 * <li>UNPAID(1): 待付款</li>
 * <li>PAID(2): 已支付 待使用</li>
 * <li>IN_USE(3): 使用中</li>
 * <li>COMPLETED(4): 已完成</li>
 * <li>CANCELED(5): 已取消</li>
 * <li>REFUNDED(6): 已退款</li>
 * <li>OWING(7): 欠费</li>
 * </ul>
 */
public enum ReserveOrderStatus {
    INACTIVE((byte)0, "无效"),
    UNPAID((byte)1, "待付款"),
    PAID((byte)2, "待使用"),
    IN_USE((byte)3, "使用中"),
    COMPLETED((byte)4, "已完成"),
    CANCELED((byte)5, "已取消"),
    REFUNDED((byte)6, "已退款"),
    OWING((byte)7, "欠费");

    private byte code;
    private String description;

    private ReserveOrderStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveOrderStatus fromCode(Byte code) {
        if(code != null) {
            ReserveOrderStatus[] values = ReserveOrderStatus.values();
            for(ReserveOrderStatus value : values) {
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
