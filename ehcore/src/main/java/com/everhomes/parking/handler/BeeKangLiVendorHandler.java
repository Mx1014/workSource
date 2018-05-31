// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.parking.ParkingLot;
import com.everhomes.parking.ParkingRechargeOrder;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.rest.parking.ListCardTypeCommand;
import com.everhomes.rest.parking.ListCardTypeResponse;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/29 11:09
 * 停车缴费V6.5.4（康利项目对接）
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX+"BEE_KANGLI")
public class BeeKangLiVendorHandler extends BeeVendorHandler {

    @Override
    public String getParkingSysPrivatekey() {
        return null;
    }

    @Override
    public String getParkingSysHost() {
        return null;
    }

    @Override
    protected void processMapParams(TreeMap<String, Object> tmap) {

    }

    @Override
    protected void processJSONParams(JSONObject params) {

    }
}
