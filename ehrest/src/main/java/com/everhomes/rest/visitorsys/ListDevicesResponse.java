// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>deviceList: (必填)设备列表，{@link com.everhomes.rest.visitorsys.BaseDeviceDTO}</li>
 * </ul>
 */
public class ListDevicesResponse {
    @ItemType(BaseDeviceDTO.class)
    private List<BaseDeviceDTO> deviceList;

    public List<BaseDeviceDTO> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<BaseDeviceDTO> deviceList) {
        this.deviceList = deviceList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}