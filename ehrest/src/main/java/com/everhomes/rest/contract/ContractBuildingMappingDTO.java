package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

public class ContractBuildingMappingDTO {
	
	private Long contractId;
	private String buildingName;
	private String apartmentName;
	
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getApartmentName() {
		return apartmentName;
	}
	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
