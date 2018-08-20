// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值月卡申请状态
 * <li>INACTIVE(0): 已取消</li>
 * <li>AUDITING(1): 待审核</li>
 * <li>QUEUEING(2): 排队中</li>
 * <li>PROCESSING(3): 待办理</li>
 * <li>SUCCEED(4): 办理成功</li>
 * <li>OPENED(5): 已开通</li>
 * </ul>
 */
public enum ParkingCardRequestStatus {
	INACTIVE((byte)0,"已取消"), AUDITING((byte)1,"待审核"), QUEUEING((byte)2,"排队中"), PROCESSING((byte)3,"待办理"), SUCCEED((byte)4,"办理成功"), OPENED((byte)5,"已开通");
    
    private byte code;
    private String desc;

    ParkingCardRequestStatus(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public byte getCode() {
        return this.code;
    }

    public String getDesc() {
        return desc;
    }

    public static ParkingCardRequestStatus fromCode(Byte code) {
        if(code != null) {
            ParkingCardRequestStatus[] values = ParkingCardRequestStatus.values();
            for(ParkingCardRequestStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
