package com.everhomes.rest.community;

/**
 * <ul>
 *  <li>communtiyName:楼栋名</li>
 *  <li>name:楼栋名</li>
 *  <li>aliasName:楼栋别名</li>
 *  <li>managerName:联系人</li>
 *  <li>contact:联系电话</li>
 *  <li>address:地址</li>
 *  <li>areaSize:占地面积</li>
 *  <li>latitudeLongitude:经纬度</li>
 *  <li>buildingNumber:楼栋编号</li>
 *  <li>trafficDescription:交通说明</li>
 *  <li>trafficDescription:楼栋介绍</li>
 * </ul>
 * Created by ying.xiong on 2018/1/22.
 */
public class BuildingExportDetailDTO {
    private String communtiyName;

    private String name;

    private String buildingNumber;

    private String aliasName;

    private String address;

    private String latitudeLongitude;

    private Double areaSize;

    private String contact;

    private String managerName;
    
    private String trafficDescription;
    
    private String description;

    public String getTrafficDescription() {
		return trafficDescription;
	}

	public void setTrafficDescription(String trafficDescription) {
		this.trafficDescription = trafficDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Double areaSize) {
        this.areaSize = areaSize;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getCommuntiyName() {
        return communtiyName;
    }

    public void setCommuntiyName(String communtiyName) {
        this.communtiyName = communtiyName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLatitudeLongitude() {
        return latitudeLongitude;
    }

    public void setLatitudeLongitude(String latitudeLongitude) {
        this.latitudeLongitude = latitudeLongitude;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
