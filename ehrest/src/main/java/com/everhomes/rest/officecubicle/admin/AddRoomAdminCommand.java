package com.everhomes.rest.officecubicle.admin;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId:域空间Id</li>
 * <li>spaceId: 空间id</li>
 * <li>siteName: 名称</li>
 * <li>communityId: 所属园区Id</li>
 * <li>description: 详情</li>
 * <li>coverUri: 封面图uri</li>
 * <li>price</li>
 * <li>rentFlag:是否开放预定 1是 0否</li>
 * <li>currentPMId: 当前管理公司ID(organizationID)</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * <li>appId: 应用id</li>
 * <li>associateStation:关联的工位ID{@link com.everhomes.rest.officecubicle.admin.AssociateStationDTO}</li>
 * </ul>
 */
public class AddRoomAdminCommand {

	@NotNull
	private String stationName;
	@NotNull
	private BigDecimal price;
	private Long spaceId;
	private String ownerType;
	private Long ownerId;
	private Byte rentFlag;
	private String description;
	private String coverUri;
	private Long currentPMId;
	private Long currentProjectId;
	private Long appId;
	private Integer namespaceId;
	private List<AssociateStationDTO> associateStation;
	private String roomName;
	

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}

	public Byte getRentFlag() {
		return rentFlag;
	}

	public void setRentFlag(Byte rentFlag) {
		this.rentFlag = rentFlag;
	}


	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoverUri() {
		return coverUri;
	}

	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
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

	public List<AssociateStationDTO> getAssociateStation() {
		return associateStation;
	}

	public void setAssociateStation(List<AssociateStationDTO> associateStation) {
		this.associateStation = associateStation;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	
}