package com.everhomes.rest.openapi.techpark;

import com.everhomes.util.StringHelper;

public class CustomerBuilding {
    private String buildingName;
    private String buildingNumber;
    private String productType;
    private String completeDate;
    private String joininDate;
    private String floorCount;
    private Boolean dealed;

    public CustomerBuilding() {
		super();
	}

	public CustomerBuilding(String buildingName) {
		super();
		this.buildingName = buildingName;
	}

	public CustomerBuilding(String buildingName, String buildingNumber, String productType, String completeDate,
			String joininDate, String floorCount) {
		super();
		this.buildingName = buildingName;
		this.buildingNumber = buildingNumber;
		this.productType = productType;
		this.completeDate = completeDate;
		this.joininDate = joininDate;
		this.floorCount = floorCount;
	}

	public Boolean getDealed() {
		return dealed;
	}

	public void setDealed(Boolean dealed) {
		this.dealed = dealed;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public String getJoininDate() {
		return joininDate;
	}

	public void setJoininDate(String joininDate) {
		this.joininDate = joininDate;
	}

	public String getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(String floorCount) {
		this.floorCount = floorCount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
