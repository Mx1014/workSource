package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

public class ParkingCarSerieDTO {
	private Long id;
	private Long parentId;
	private String name;
	private String path;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
