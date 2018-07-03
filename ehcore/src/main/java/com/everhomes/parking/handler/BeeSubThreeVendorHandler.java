// @formatter:off
package com.everhomes.parking.handler;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.rest.parking.ParkingLotVendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

/**
 * @Author dengs[shuang.deng@zuolin.com]
 * @Date 2018/5/29 11:09
 * 停车缴费V6.5.4 蜜蜂预留停车场
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX+"BEE_SUBTHREE")
public class BeeSubThreeVendorHandler extends BeeVendorHandler{
        @Autowired
        ConfigurationProvider configProvider;
        @Override
        public String getParkingPloid() {
            String ploid = configProvider.getValue("parking.subthree.ploid", "");
            return ploid;
        }
        @Override
        public String getParkingSysPrivatekey() {
            String privatekey = configProvider.getValue("parking.subthree.privatekey", "");
            return privatekey;
        }

        @Override
        public String getParkingSysHost() {
            String url = configProvider.getValue("parking.subthree.url", "");
            return url;
        }

        @Override
        protected void processMapParams(TreeMap<String, Object> tmap) {
            String clientType = configProvider.getValue("parking.subthree.clientType", "java");
            String method = configProvider.getValue("parking.subthree.method", "doActivity");
            String module = configProvider.getValue("parking.subthree.module", "activity");

            String service = configProvider.getValue("parking.subthree.service", "Entrance");
            String typeflag = configProvider.getValue("parking.subthree.typeflag", "ThirdQuery");
            String gcode = configProvider.getValue("parking.subthree.gcode", "");
            tmap.put("clientType",clientType);
            tmap.put("method",method);
            tmap.put("module",module);

            tmap.put("service",service);
            tmap.put("typeflag",typeflag);
            tmap.put("gcode",gcode);
        }

        @Override
        protected void processJSONParams(JSONObject params) {
            String comid = configProvider.getValue("parking.subthree.comid", "");
            params.put("comid",comid);
        }
        
        @Override
        public String getParkingVendorName() {
        	return ParkingLotVendor.BEE_SUBTHREE.getCode();
        }
}
