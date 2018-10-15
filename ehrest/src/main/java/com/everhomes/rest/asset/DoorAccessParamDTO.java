package com.everhomes.rest.asset;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * @author created by djm
 * 
 */
public class DoorAccessParamDTO {
	private Long id;
	private Long ownerId;
	private String ownerType;
	private Long orgId;
	private Byte paramsStatus;
	private Long arrearageDays;
	private Long categoryId;
	private String dooraccessList;
	@ItemType(DooraccessList.class)
    private List<DooraccessList> dooraccess;
	
	public String getDooraccessList() {
		return dooraccessList;
	}
	public void setDooraccessList(String dooraccessList) {
		this.dooraccessList = dooraccessList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Byte getParamsStatus() {
		return paramsStatus;
	}
	public void setParamsStatus(Byte paramsStatus) {
		this.paramsStatus = paramsStatus;
	}
	public Long getArrearageDays() {
		return arrearageDays;
	}
	public void setArrearageDays(Long arrearageDays) {
		this.arrearageDays = arrearageDays;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public List<DooraccessList> getDooraccess() {
		return dooraccess;
	}
	public void setDooraccess(List<DooraccessList> dooraccess) {
		this.dooraccess = dooraccess;
	}
}
