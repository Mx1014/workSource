// @formatter:off
package com.everhomes.rest.promotion;

/**
 * <ul>运营推广活动状态
 * <li>INACTIVE(0): 关闭</li>
 * <li>WAITING_FOR_APPROVAL(1): 待审批</li>
 * <li>ACTIVE(2): 推送中</li>
 * <li>COMPLETE(3): 推送完成</li>
 * </ul>
 */
public enum OpPromotionStatus {
    INACTIVE((byte)0), WAITING_FOR_APPROVAL((byte)1), ACTIVE((byte)2), COMPLETE((byte)3);
    
    private byte code;
    
    private OpPromotionStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static OpPromotionStatus fromCode(Byte code) {
        if(code != null) {
            OpPromotionStatus[] values = OpPromotionStatus.values();
            for(OpPromotionStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
