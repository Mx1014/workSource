// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.bee.BeeResponse;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/29 11:09
 *   #26499 停车缴费V6.5.3（中天项目对接）
 *
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX+"BEE_ZHONGTIAN")
public class BeeZhongTianVendorHandler extends BeeVendorHandler {
    @Autowired
    ConfigurationProvider configProvider;
    @Override
    public String getParkingSysPrivatekey() {
        String url = configProvider.getValue("parking.beezhongtian.privatekey", "");
        return url;
    }

    @Override
    public String getParkingSysHost() {
        String url = configProvider.getValue("parking.beezhongtian.url", "http://www.szkljy.com/trade/data");
        return url;
    }
}
