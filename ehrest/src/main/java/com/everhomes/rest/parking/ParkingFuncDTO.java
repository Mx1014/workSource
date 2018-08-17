// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
  *<ul>V6.6
  *<li>code : 功能枚举，参考{@link  com.everhomes.rest.parking.ParkingBusinessType}</li>
  *<li>enableFlag : 功能是否启用, 0：不启用，1：启用 {@link ParkingConfigFlag}</li>
  *</ul>
  */

public class ParkingFuncDTO {
    private String code;
    private Byte enableFlag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Byte getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Byte enableFlag) {
        this.enableFlag = enableFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
