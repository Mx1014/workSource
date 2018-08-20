package com.everhomes.rest.yellowPage.stat;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serviceName: 服务名称</li>
 * <li>serviceTypeName: 服务类型名称</li>
 * <li>interestStat: 兴趣指数，因后续可能会变化，故传字符串</li>
 * </ul>
 */
public class InterestStatDTO {
	private String serviceName;
	private String serviceTypeName;
	private String interestStat;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getInterestStat() {
		return interestStat;
	}
	public void setInterestStat(String interestStat) {
		this.interestStat = interestStat;
	}

	public String getServiceTypeName() {
		return serviceTypeName;
	}

	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}
	
	
}
