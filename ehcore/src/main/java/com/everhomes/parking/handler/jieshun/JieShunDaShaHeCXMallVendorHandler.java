package com.everhomes.parking.handler.jieshun;

import org.springframework.stereotype.Component;
import com.everhomes.parking.ParkingVendorHandler;
import com.everhomes.parking.handler.Utils;
import com.everhomes.rest.parking.VendorNameEnum;

@Component(ParkingVendorHandler.PARKING_VENDOR_PREFIX + JieShunDaShaHeCXMallVendorHandler.VENDOR_NAME)
public class JieShunDaShaHeCXMallVendorHandler extends JieShunVendorHandler{
	
	static final String VENDOR_NAME = VendorNameEnum.JIESHUN_DSHCXMall;
	
	@Override
	//大沙河按自然月份计算
	public long getMonthlyRechargeEndTime(long newStart, int monthCount) {
		return Utils.getLongByAddNatureMonth(newStart, monthCount);
	}

	@Override
	public String getParkingVendorName() {
		return VENDOR_NAME;
	}
	
}
