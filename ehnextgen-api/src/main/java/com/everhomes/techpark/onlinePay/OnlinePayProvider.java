package com.everhomes.techpark.onlinePay;

import com.everhomes.techpark.park.RechargeInfo;

public interface OnlinePayProvider {

	RechargeInfo findRechargeInfoByOrderId(Long orderId);
	void updateRehargeInfo(RechargeInfo rechargeOrder);
}
