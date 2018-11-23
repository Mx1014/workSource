package com.everhomes.parking.handler.jieshun;

import org.springframework.stereotype.Component;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.rest.parking.VendorNameEnum;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + JieShunGQY2VendorHandler.VENDOR_NAME)
public class JieShunGQY2VendorHandler extends JieShunVendorHandler{
	
	static final String VENDOR_NAME = VendorNameEnum.JIESHUN_GQY2;
	
	@Override
	public String getParkingVendorName() {
		return VENDOR_NAME;
	}
	
}
