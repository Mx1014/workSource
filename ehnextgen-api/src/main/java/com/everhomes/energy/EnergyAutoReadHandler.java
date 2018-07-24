package com.everhomes.energy;

/**
 * Created by Rui.Jia  2018/4/19 13 :21
 */

public interface EnergyAutoReadHandler {
    String AUTO_PREFIX = "autoReading";
    String ZHI_FU_HUI = "zhi_fu_hui";

    String readMeterautomatically(String meterName, String serverUrl, String publicKey, String clientId);
}
