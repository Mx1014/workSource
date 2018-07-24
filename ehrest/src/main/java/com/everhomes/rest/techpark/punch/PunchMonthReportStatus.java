package com.everhomes.rest.techpark.punch;

/**
 * <ul>月报的状态
 * <li>UPDATING(0): 更新中</li>
 * <li>FILED(2): 已归档不可改</li>
 * <li>CREATED(1): 创建完成 可以归档可以更新</li>
 * </ul>
 */
public enum PunchMonthReportStatus {
	FILED((byte)2),
	CREATED((byte)1),
	UPDATING((byte)0) ;

    private byte code;
    private PunchMonthReportStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchMonthReportStatus fromCode(Byte code) {
        for (PunchMonthReportStatus t : PunchMonthReportStatus.values()) {
            if (null == code) {
                return null;
            }
            if (t.code == code.byteValue()) {
                return t;
            }
        }
        
        return null;
    }
}
