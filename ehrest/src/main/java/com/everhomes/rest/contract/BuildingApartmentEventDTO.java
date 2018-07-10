//@formatter:off
package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
* 
* <ul>
* <li>id: id</li>
* <li>buildingName: 楼栋</li>
* <li>apartmentName: 门牌</li>
* <li>addressId: 门牌id</li>
* <li>chargeArea: 收费面积</li>
* </ul>
*/
public class BuildingApartmentEventDTO {
	private Long id;
	private Long addressId;
	private String buildingName;
	private String apartmentName;
	private Double chargeArea;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getChargeArea() {
		return chargeArea;
	}

	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressId == null) ? 0 : addressId.hashCode());
		result = prime * result + ((apartmentName == null) ? 0 : apartmentName.hashCode());
		result = prime * result + ((buildingName == null) ? 0 : buildingName.hashCode());
		result = prime * result + ((chargeArea == null) ? 0 : chargeArea.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildingApartmentEventDTO other = (BuildingApartmentEventDTO) obj;
		if (addressId == null) {
			if (other.addressId != null)
				return false;
		} else if (!addressId.equals(other.addressId))
			return false;
		if (apartmentName == null) {
			if (other.apartmentName != null)
				return false;
		} else if (!apartmentName.equals(other.apartmentName))
			return false;
		if (buildingName == null) {
			if (other.buildingName != null)
				return false;
		} else if (!buildingName.equals(other.buildingName))
			return false;
		if (chargeArea == null) {
			if (other.chargeArea != null)
				return false;
		} else if (!chargeArea.equals(other.chargeArea))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
