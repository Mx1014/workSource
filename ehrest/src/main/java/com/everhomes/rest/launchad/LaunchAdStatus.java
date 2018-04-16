// @formatter:off
package com.everhomes.rest.launchad;

import com.everhomes.rest.energy.EnergyMeterStatus;

/**
 * <ul>
 *     <li>INACTIVE((byte) 0): 关闭</li>
 *     <li>WAITING_FOR_APPROVAL((byte) 1): WAITING_FOR_APPROVAL</li>
 *     <li>ACTIVE((byte) 2): 开启</li>
 * </ul>
 */
public enum LaunchAdStatus {

    INACTIVE((byte) 0), WAITING_FOR_APPROVAL((byte) 1), ACTIVE((byte) 2);

    private Byte code;

    LaunchAdStatus(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

    public static EnergyMeterStatus fromCode(Byte code) {
        for (EnergyMeterStatus type : EnergyMeterStatus.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
