package com.everhomes.parking.handler.haikangweishi;

import org.springframework.stereotype.Component;

import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.rest.parking.VendorNameEnum;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + HaiKangWeiShiJinMao2VendorHandler.VENDOR_NAME)
public class HaiKangWeiShiJinMao2VendorHandler extends HaiKangWeiShiVendorHandler{
	static final String VENDOR_NAME = VendorNameEnum.HKWS_SHJINMAO2;
	
	@Override
	public String getParkingVendorName() {
		return VENDOR_NAME;
	}
}
