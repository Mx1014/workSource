// @formatter:off
package com.everhomes.rest.parking.clearance;

/**
 * <ul>
 *     <li>APPLICANT("APPLICANT"): 申请人</li>
 *     <li>PROCESSOR("PROCESSOR")：处理人 已废弃</li>
 * </ul>
 */
public enum ParkingClearanceOperatorType {

    APPLICANT("APPLICANT"), @Deprecated PROCESSOR("PROCESSOR");

    private String code;

    ParkingClearanceOperatorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ParkingClearanceOperatorType fromCode(String code) {
        for (ParkingClearanceOperatorType type : ParkingClearanceOperatorType.values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
