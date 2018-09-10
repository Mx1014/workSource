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
import com.everhomes.rest.parking.ParkingLotVendor;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeMap;

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
    public String getParkingPloid() {
        String ploid = configProvider.getValue("parking.beezhongtian.ploid", "20180516100018550498928837853806");
        return ploid;
    }

    @Override
    public String getParkingSysPrivatekey() {
        String privatekey = configProvider.getValue("parking.beezhongtian.privatekey", "D3029C73406221B02026B684BB00579C");
        return privatekey;
    }

    @Override
    public String getParkingSysHost() {
        String url = configProvider.getValue("parking.beezhongtian.url", "http://www.szkljy.com/trade/data");
        return url;
    }

    @Override
    protected void processMapParams(TreeMap<String, Object> tmap) {
        String clientType = configProvider.getValue("parking.beezhongtian.clientType", "java");
        String method = configProvider.getValue("parking.beezhongtian.method", "doActivity");
        String module = configProvider.getValue("parking.beezhongtian.module", "activity");

        String service = configProvider.getValue("parking.beezhongtian.service", "Entrance");
        String typeflag = configProvider.getValue("parking.beezhongtian.typeflag", "ThirdQuery");
        String gcode = configProvider.getValue("parking.beezhongtian.gcode", "ZTGJ001");
        tmap.put("clientType",clientType);
        tmap.put("method",method);
        tmap.put("module",module);

        tmap.put("service",service);
        tmap.put("typeflag",typeflag);
        tmap.put("gcode",gcode);
    }

    @Override
    protected void processJSONParams(JSONObject params) {
        String comid = configProvider.getValue("parking.beezhongtian.comid", "000000771");
        params.put("comid",comid);
    }
    
    @Override
    public String getParkingVendorName() {
    	return ParkingLotVendor.BEE_ZHONGTIAN.getCode();
    }
}
