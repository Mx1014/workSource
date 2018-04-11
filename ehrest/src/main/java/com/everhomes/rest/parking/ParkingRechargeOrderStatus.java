// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值订单状态 
 * <li>UNPAID(1): 待付款</li>
 * <li>PAID(2): 已付款（客户已经成功付款，但系统未完成业务处理）</li> 
 * <li>RECHARGED(3): 已完成</li>
 * <li>FAILED(4): 订单异常</li>
 * <li>REFUNDING(5): 退款中</li>
 * <li>REFUNDED(6): 已退款</li>
 * </ul>
 */
public enum ParkingRechargeOrderStatus {
    /*INACTIVE((byte)0, "无效"), */UNPAID((byte)1, "待付款"), PAID((byte)2, "已付款"), RECHARGED((byte)3, "已完成"),
    FAILED((byte)4, "订单异常"), REFUNDING((byte)5, "退款中"), REFUNDED((byte)6, "已退款");
    
    private byte code;
    private String description;
    
    private ParkingRechargeOrderStatus(byte code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingRechargeOrderStatus fromCode(Byte code) {
        if(code != null) {
            ParkingRechargeOrderStatus[] values = ParkingRechargeOrderStatus.values();
            for(ParkingRechargeOrderStatus value : values) {
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
