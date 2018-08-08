package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * Created by Rui.Jia  2018/4/17 13 :39
 */

public enum EnergyAutoReadingFlag {
    FALSE((byte) 0), TURE((byte) 1);
    private Byte code;

    EnergyAutoReadingFlag(Byte code) {
        this.code = code;
    }

    public Byte getCode() {
        return code;
    }

   public static EnergyAutoReadingFlag fromStatus(Byte code) {
        for (EnergyAutoReadingFlag val : EnergyAutoReadingFlag.values()) {
            if (val.getCode().equals(code)) {
                return val;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
