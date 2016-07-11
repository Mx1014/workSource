// @formatter:off
package com.everhomes.parking;

import com.everhomes.server.schema.tables.pojos.EhParkingVendors;
import com.everhomes.util.StringHelper;

public class ParkingVendor extends EhParkingVendors {
    private static final long serialVersionUID = 3117248189635881815L;

    public ParkingVendor() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
