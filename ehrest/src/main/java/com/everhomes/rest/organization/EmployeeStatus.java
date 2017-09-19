package com.everhomes.rest.organization;

/**
 * <p>员工类型</p>
 * <ul>
 * <li>PROBATION(0): 试用期</li>
 * <li>ONTHEJOB(1): 在职</li>
 * <li>INTERSHIP(2): 实习</li>
 * </ul>
 */
public enum EmployeeStatus {
    PROBATION((byte)0), ONTHEJOB((byte)1), INTERSHIP((byte)2);

    private byte code;

    private EmployeeStatus(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static EmployeeStatus fromCode(Byte code) {
        if(code != null) {
            EmployeeStatus[] values = EmployeeStatus.values();
            for(EmployeeStatus value : values) {
                if(code.byteValue() == value.code) {
                    return value;
                }
            }
        }
        return null;
    }
}