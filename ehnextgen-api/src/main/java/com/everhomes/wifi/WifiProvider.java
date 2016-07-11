package com.everhomes.wifi;

import java.util.List;

public interface WifiProvider {
	List<WifiSetting> listWifiSetting(Long ownerId,String ownerType,Long pageAnchor,Integer pageSize);
	void createWifiSetting(WifiSetting wifiSetting);
	void updateWifiSetting(WifiSetting wifiSetting);
	WifiSetting findWifiSettingById(Long id);
	WifiSetting findWifiSettingByCondition(String ssid,Long ownerId,String ownerType);
}
