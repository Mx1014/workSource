package com.everhomes.yellowPage.faq;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>serviceId : 服务id</li>
 * <li>serviceName : 服务名称</li>
 * <li>serviceTypeName : 服务类型名称</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月23日
 */
public class OperateServiceDTO {
	private Long serviceId;
	private String serviceName;
	private String serviceTypeName;
	
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceTypeName() {
		return serviceTypeName;
	}
	public void setServiceTypeName(String serviceTypeName) {
		this.serviceTypeName = serviceTypeName;
	}
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
