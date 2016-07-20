package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>ownerId: 备件所属的主体id</li>
 *  <li>ownerType: 备件所属的主体，参考{@link com.everhomes.rest.quality.OwnerType}</li>
 *  <li>id: 备件id</li>
 *  <li>name: 备件名称</li>
 *  <li>manufacturer: 生产厂商</li>
 *  <li>equipmentModel: 备品型号</li>
 *  <li>specification: 规格</li>
 *  <li>location: 存放地点</li>
 * </ul>
 */
public class EquipmentAccessoriesDTO {
	
	private Long id;
	
	private String ownerType;
	
	private Long ownerId;
	
	private String name;
	
	private String manufacturer;
	
	private String modelNumber;
	
	private String specification;
	
	private String location;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
