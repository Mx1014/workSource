package com.everhomes.techpark.onlinePay;

import com.everhomes.rest.techpark.onlinePay.OnlinePayBillCommand;
import com.everhomes.rest.techpark.park.RechargeInfoDTO;


public interface OnlinePayService {

	RechargeInfoDTO onlinePayBill(OnlinePayBillCommand cmd);
	
	Long createBillId(Long time);
	
}
