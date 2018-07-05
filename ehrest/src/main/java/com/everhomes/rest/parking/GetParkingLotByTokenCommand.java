// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
  *<ul>
  *<li>parkingLotToken : 停车场id的token</li>
  *</ul>
  */

public class GetParkingLotByTokenCommand {
    private String parkingLotToken;

    public String getParkingLotToken() {
        return parkingLotToken;
    }

    public void setParkingLotToken(String parkingLotToken) {
        this.parkingLotToken = parkingLotToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
