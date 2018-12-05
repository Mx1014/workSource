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
 * 创科谷园区
 */
@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX+"BEE_CHUANGKEGU")
public class BeeChuangKeGuVendorHandler extends BeeVendorHandler{
        @Autowired
        ConfigurationProvider configProvider;
        @Override
        public String getParkingPloid() {
            String ploid = configProvider.getValue("parking.chuangkegu.ploid", "");
            return ploid;
        }
        @Override
        public String getParkingSysPrivatekey() {
            String privatekey = configProvider.getValue("parking.chuangkegu.privatekey", "");
            return privatekey;
        }

        @Override
        public String getParkingSysHost() {
            String url = configProvider.getValue("parking.chuangkegu.url", "");
            return url;
        }

        @Override
        protected void processMapParams(TreeMap<String, Object> tmap) {
            String clientType = configProvider.getValue("parking.chuangkegu.clientType", "java");
            String method = configProvider.getValue("parking.chuangkegu.method", "doActivity");
            String module = configProvider.getValue("parking.chuangkegu.module", "activity");

            String service = configProvider.getValue("parking.chuangkegu.service", "Entrance");
            String typeflag = configProvider.getValue("parking.chuangkegu.typeflag", "ThirdQuery");
            String gcode = configProvider.getValue("parking.chuangkegu.gcode", "");
            tmap.put("clientType",clientType);
            tmap.put("method",method);
            tmap.put("module",module);

            tmap.put("service",service);
            tmap.put("typeflag",typeflag);
            tmap.put("gcode",gcode);
        }

        @Override
        protected void processJSONParams(JSONObject params) {
            String comid = configProvider.getValue("parking.chuangkegu.comid", "");
            params.put("comid",comid);
        }
        
        @Override
        public String getParkingVendorName() {
        	return ParkingLotVendor.BEE_CHUANGKEGU.getCode();
        }
}
