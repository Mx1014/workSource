package com.everhomes.yellowPage.faq;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id : id</li>
 * <li>serviceId : 服务id</li>
 * <li>serviceName : 服务名称</li>
 * <li>serviceTypeName : 服务类型名称</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月23日
 */
public class OperateServiceDTO {
	private Long id;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
