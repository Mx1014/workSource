package com.everhomes.rest.techpark.punch;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>wifis: wifi mac地址 {@link com.everhomes.rest.techpark.punch.admin.PunchWiFiDTO}</li>
 * <li>geoPoints: 经纬度及打卡半径信息 {@link com.everhomes.rest.techpark.punch.PunchGeoPointDTO}</li>
 * </ul>
 */
public class ListPunchSupportiveAddressCommandResponse {

    @ItemType(PunchWiFiDTO.class)
    private List<PunchWiFiDTO> wifis;

    @ItemType(PunchGeoPointDTO.class)
    private List<PunchGeoPointDTO> geoPoints;

    public ListPunchSupportiveAddressCommandResponse() {
    }

    public List<PunchWiFiDTO> getWifis() {
        return wifis;
    }

    public void setWifis(List<PunchWiFiDTO> wifis) {
        this.wifis = wifis;
    }

    public List<PunchGeoPointDTO> getGeoPoints() {
        return geoPoints;
    }

    public void setGeoPoints(List<PunchGeoPointDTO> geoPoints) {
        this.geoPoints = geoPoints;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
