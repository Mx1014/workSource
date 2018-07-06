package com.everhomes.rest.portal;

import com.everhomes.rest.parking.ParkingFuncDTO;
import com.everhomes.rest.parking.ParkingLotFuncConfig;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *
 * <ul>
 * <li>resourceTypeId: 资源类型id</li>
 * <li>parkingLotFuncConfigs: 各个停车场对接的功能列表，{@link com.everhomes.rest.parking.ParkingLotFuncConfig}</li>
 * </ul>
 */
public class ParkingInstanceConfig {

    private Long resourceTypeId;

    private List<ParkingLotFuncConfig> parkingLotFuncConfigs;

    public Long getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(Long resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public List<ParkingLotFuncConfig> getParkingLotFuncConfigs() {
        return parkingLotFuncConfigs;
    }

    public void setParkingLotFuncConfigs(List<ParkingLotFuncConfig> parkingLotFuncConfigs) {
        this.parkingLotFuncConfigs = parkingLotFuncConfigs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
