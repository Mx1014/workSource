package com.everhomes.rest.investmentAd;

public interface InvestmentAdErrorCode {

	String SCOPE = "investmentAd";
	
	int ERROR_NO_DATA = 100000;//导出时，无招商广告数据
	int INVESTMENTAD_NOT_FOUND = 100001;//招商广告不存在
	
}
