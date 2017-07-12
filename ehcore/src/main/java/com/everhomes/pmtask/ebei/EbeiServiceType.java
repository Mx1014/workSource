package com.everhomes.pmtask.ebei;

import java.util.List;

public class EbeiServiceType {
	
	private String parentId;
	
	private String serviceCode;
	
	private String serviceId;
	
	private String serviceName;
	
	private List<EbeiServiceType> items;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<EbeiServiceType> getItems() {
		return items;
	}

	public void setItems(List<EbeiServiceType> items) {
		this.items = items;
	}
	
	
}
