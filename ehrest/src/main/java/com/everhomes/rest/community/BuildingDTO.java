package com.everhomes.rest.community;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * 
 * 楼栋信息
 * <ul>
 *  <li>id:楼栋id</li>
 *  <li>namespaceId: 域空间id</li>
 *  <li>communityId:楼栋所属园区or小区id</li>
 *  <li>name:楼栋名</li>
 *  <li>aliasName:楼栋别名</li>
 *  <li>managerUid:楼栋管理员id</li>
 *  <li>managerNickName:管理员昵称</li>
 *  <li>managerAvatar:管理员头像URI</li>
 *  <li>managerAvatarUrl:管理员头像URL</li>
 *  <li>contact:联系电话</li>
 *  <li>address:地址</li>
 *  <li>areaSize:占地面积</li>
 *  <li>longitude:经度</li>
 *  <li>latitude:纬度</li>
 *  <li>description:楼栋介绍</li>
 *  <li>posterUrl:海报链接</li>
 *  <li>operatorUid:审核楼栋地址用户id</li>
 *  <li>operateNickName:审核楼栋地址用户昵称</li>
 *  <li>operateAvatar:审核楼栋地址用户头像URI</li>
 *  <li>operateAvatarUrl:审核楼栋地址用户头像URL</li>
 *  <li>operateTime:审核时间</li>
 *  <li>creatorUid:创建楼栋地址用户id</li>
 *  <li>creatorNickName:创建楼栋地址用户昵称</li>
 *  <li>creatorAvatar:创建楼栋地址用户头像URI</li>
 *  <li>creatorAvatarUrl:创建楼栋地址用户头像URL</li>
 *  <li>createTime:创建楼栋地址时间</li>
 *  <li>attachments:楼栋附件信息</li>
 *  <li>buildingNumber:楼栋编号</li>
 *  <li>trafficDescription:交通说明</li>
 *  <li>rentArea:在租面积</li>
 *  <li>freeArea:可招租面积</li>
 *  <li>chargeArea:收费面积</li>
 *  <li>floorNumber:楼层数量</li>
 * </ul>
 */
public class BuildingDTO {

	private Long id; 

    private Integer namespaceId;
	
	private Long communityId;
	
	private String name;
	
	private String buildingName;
	
	private String aliasName;

	private String buildingNumber;
	
	private Long managerUid;

	private String managerName;

	private String managerNickName;
    
	private String managerContact;
	
    private String managerAvatar;
    
    private String managerAvatarUrl;
	
	private String contact;
	
	private String address;
	
	private Double areaSize;
	
	private Double longitude;
	
	private Double latitude;
	
	private String description;

	private String posterUrl;
	private String posterUri;
	
	private Long operatorUid;
	
	private Timestamp operateTime;
	
	private String operateNickName;
    
    private String operateAvatar;
    
    private String operateAvatarUrl;
    
	private Long creatorUid;
	
	private String creatorNickName;
    
    private String creatorAvatar;
    
    private String creatorAvatarUrl;
	
	private Timestamp createTime;
	
	private String detailUrl;

	private String floorCount;
	private String trafficDescription;

	private int apartmentCount;
	
	@ItemType(BuildingAttachmentDTO.class)
	private List<BuildingAttachmentDTO> attachments;

	private String lastTime;
	
	private Double rentArea;
	private Double freeArea;
	private Double chargeArea;
	private Integer floorNumber;
	
	public Double getRentArea() {
		return rentArea;
	}

	public void setRentArea(Double rentArea) {
		this.rentArea = rentArea;
	}

	public Double getFreeArea() {
		return freeArea;
	}

	public void setFreeArea(Double freeArea) {
		this.freeArea = freeArea;
	}

	public Double getChargeArea() {
		return chargeArea;
	}

	public void setChargeArea(Double chargeArea) {
		this.chargeArea = chargeArea;
	}

	public Integer getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getPosterUri() {
		return posterUri;
	}

	public void setPosterUri(String posterUri) {
		this.posterUri = posterUri;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getOperateNickName() {
		return operateNickName;
	}

	public void setOperateNickName(String operateNickName) {
		this.operateNickName = operateNickName;
	}

	public String getOperateAvatar() {
		return operateAvatar;
	}

	public void setOperateAvatar(String operateAvatar) {
		this.operateAvatar = operateAvatar;
	}

	public String getOperateAvatarUrl() {
		return operateAvatarUrl;
	}

	public void setOperateAvatarUrl(String operateAvatarUrl) {
		this.operateAvatarUrl = operateAvatarUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public Long getManagerUid() {
		return managerUid;
	}

	public void setManagerUid(Long managerUid) {
		this.managerUid = managerUid;
	}

	public String getManagerNickName() {
		return managerNickName;
	}

	public void setManagerNickName(String managerNickName) {
		this.managerNickName = managerNickName;
	}

	public String getManagerAvatar() {
		return managerAvatar;
	}

	public void setManagerAvatar(String managerAvatar) {
		this.managerAvatar = managerAvatar;
	}

	public String getManagerAvatarUrl() {
		return managerAvatarUrl;
	}

	public void setManagerAvatarUrl(String managerAvatarUrl) {
		this.managerAvatarUrl = managerAvatarUrl;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Double areaSize) {
		this.areaSize = areaSize;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public Timestamp getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}

	public Long getCreatorUid() {
		return creatorUid;
	}

	public void setCreatorUid(Long creatorUid) {
		this.creatorUid = creatorUid;
	}

	public String getCreatorNickName() {
		return creatorNickName;
	}

	public void setCreatorNickName(String creatorNickName) {
		this.creatorNickName = creatorNickName;
	}

	public String getCreatorAvatar() {
		return creatorAvatar;
	}

	public void setCreatorAvatar(String creatorAvatar) {
		this.creatorAvatar = creatorAvatar;
	}

	public String getCreatorAvatarUrl() {
		return creatorAvatarUrl;
	}

	public void setCreatorAvatarUrl(String creatorAvatarUrl) {
		this.creatorAvatarUrl = creatorAvatarUrl;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public List<BuildingAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<BuildingAttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getManagerContact() {
		return managerContact;
	}

	public void setManagerContact(String managerContact) {
		this.managerContact = managerContact;
	}

	public String getTrafficDescription() {
		return trafficDescription;
	}

	public void setTrafficDescription(String trafficDescription) {
		this.trafficDescription = trafficDescription;
	}

	public String getFloorCount() {
		return floorCount;
	}

	public void setFloorCount(String floorCount) {
		this.floorCount = floorCount;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public int getApartmentCount() {
		return apartmentCount;
	}

	public void setApartmentCount(int apartmentCount) {
		this.apartmentCount = apartmentCount;
	}
}
