package com.everhomes.parking.handler.jieshun;

import org.springframework.stereotype.Component;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.rest.parking.jieshun.VendorNameEnum;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + JieShunGQY1VendorHandler.VENDOR_NAME)
public class JieShunGQY1VendorHandler extends JieShunGQY2VendorHandler{
	
	static final String VENDOR_NAME = VendorNameEnum.JIESHUN_GQY1;
	
	@Override
	public String getParkingVendorName() {
		return VENDOR_NAME;
	}
	
}
