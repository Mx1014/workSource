package com.everhomes.wifi;

import com.everhomes.rest.wifi.CreateWifiSettingCommand;
import com.everhomes.rest.wifi.DeleteWifiSettingCommand;
import com.everhomes.rest.wifi.EditWifiSettingCommand;
import com.everhomes.rest.wifi.ListWifiSettingCommand;
import com.everhomes.rest.wifi.ListWifiSettingResponse;
import com.everhomes.rest.wifi.VerifyWifiCommand;
import com.everhomes.rest.wifi.VerifyWifiDTO;
import com.everhomes.rest.wifi.WifiSettingDTO;

public interface WifiService {
	ListWifiSettingResponse listWifiSetting(ListWifiSettingCommand cmd);
	WifiSettingDTO createWifiSetting(CreateWifiSettingCommand cmd);
	WifiSettingDTO editWifiSetting(EditWifiSettingCommand cmd);
	void deleteWifiSetting(DeleteWifiSettingCommand cmd);
	VerifyWifiDTO verifyWifi(VerifyWifiCommand cmd);
}
