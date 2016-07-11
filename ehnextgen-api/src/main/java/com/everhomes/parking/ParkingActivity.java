// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingActivities;
import com.everhomes.util.StringHelper;

public class ParkingActivity extends EhParkingActivities {
    private static final long serialVersionUID = 7522929381204816872L;

    public ParkingActivity() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
