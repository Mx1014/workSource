package com.everhomes.rest.organization;

/**
 * <p>员工状态</p>
 * <ul>
 * <li>PROBATION(0): 试用</li>
 * <li>ON_THE_JOB(1): 正式</li>
 * <li>INTERNSHIP(2): 实习</li>
 * <li>DISMISSAL(3): 离职</li>
 * </ul>
 */
public enum EmployeeStatus {
    PROBATION((byte)0), ON_THE_JOB((byte)1), INTERNSHIP((byte)2), DISMISSAL((byte)3);

    private byte code;

    EmployeeStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static EmployeeStatus fromCode(Byte code) {
        if(code != null) {
            EmployeeStatus[] values = EmployeeStatus.values();
            for(EmployeeStatus value : values) {
                if(code == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}