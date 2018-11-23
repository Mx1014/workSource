// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.rest.parking.ParkingLotVendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.TreeMap;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/29 11:09
 * 停车缴费V6.5.4 蜜蜂预留停车场
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX+"BEE_SUBONE")
public class BeeSubOneVendorHandler extends BeeVendorHandler{
        @Autowired
        ConfigurationProvider configProvider;
        @Override
        public String getParkingPloid() {
            String ploid = configProvider.getValue("parking.subone.ploid", "");
            return ploid;
        }
        @Override
        public String getParkingSysPrivatekey() {
            String privatekey = configProvider.getValue("parking.subone.privatekey", "");
            return privatekey;
        }

        @Override
        public String getParkingSysHost() {
            String url = configProvider.getValue("parking.subone.url", "");
            return url;
        }

        @Override
        protected void processMapParams(TreeMap<String, Object> tmap) {
            String clientType = configProvider.getValue("parking.subone.clientType", "java");
            String method = configProvider.getValue("parking.subone.method", "doActivity");
            String module = configProvider.getValue("parking.subone.module", "activity");

            String service = configProvider.getValue("parking.subone.service", "Entrance");
            String typeflag = configProvider.getValue("parking.subone.typeflag", "ThirdQuery");
            String gcode = configProvider.getValue("parking.subone.gcode", "");
            tmap.put("clientType",clientType);
            tmap.put("method",method);
            tmap.put("module",module);

            tmap.put("service",service);
            tmap.put("typeflag",typeflag);
            tmap.put("gcode",gcode);
        }

        @Override
        protected void processJSONParams(JSONObject params) {
            String comid = configProvider.getValue("parking.subone.comid", "");
            params.put("comid",comid);
        }
        
        @Override
        public String getParkingVendorName() {
        	return ParkingLotVendor.BEE_SUBONE.getCode();
        }
}
