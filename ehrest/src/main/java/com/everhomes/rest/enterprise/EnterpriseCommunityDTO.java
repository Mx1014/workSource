package com.everhomes.rest.enterprise;

import java.util.List;

import com.everhomes.rest.community.CommunityGeoPointDTO;
import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>id: 小区Id</li>
 * <li>uuid: 小区uuid，作为唯一标识</li>
 * <li>cityId: 城市Id</li>
 * <li>cityName: 城市名称</li>
 * <li>areaId: 区域Id</li>
 * <li>areaName: 区域名称</li>
 * <li>name: 小区名称</li>
 * <li>aliasName: 小区别名</li>
 * <li>address: 小区地址</li>
 * <li>zipcode: 邮政编码</li>
 * <li>description: 简略描述</li>
 * <li>detailDescription: 详细描述</li>
 * <li>aptCount: 公寓数</li>
 * <li>creatorUid: 创建者Id</li>
 * <li>status: 小区状态，参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 * <li>createTime: 创建时间</li>
 * <li>deleteTime: 删除时间</li>
 * <li>requestStatus: 小区收集状态，参考{@link com.everhomes.rest.community.RequestStatus}</li>
 * <li>geoPointList: 小区经纬度列表，参考{@link com.everhomes.rest.community.CommunityGeoPointDTO}</li>
 * </ul>
 */
public class EnterpriseCommunityDTO {
    private java.lang.Long     id;
    private java.lang.String   uuid;
    private java.lang.Long     cityId;
    private java.lang.String   cityName;
    private java.lang.Long     areaId;
    private java.lang.String   areaName;
    private java.lang.String   name;
    private java.lang.String   aliasName;
    private java.lang.String   address;
    private java.lang.String   zipcode;
    private java.lang.String   description;
    private java.lang.String   detailDescription;
    private java.lang.String   aptSegment1;
    private java.lang.String   aptSegment2;
    private java.lang.String   aptSegment3;
    private java.lang.String   aptSeg1Sample;
    private java.lang.String   aptSeg2Sample;
    private java.lang.String   aptSeg3Sample;
    private java.lang.Integer  aptCount;
    private java.lang.Long     creatorUid;
    private java.lang.Byte     status;
    private java.sql.Timestamp createTime;
    private java.sql.Timestamp deleteTime;
    private java.lang.Long     requestStatus;
    
    @ItemType(CommunityGeoPointDTO.class)
    private List<CommunityGeoPointDTO> geoPointList;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getUuid() {
        return uuid;
    }

    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    public java.lang.Long getCityId() {
        return cityId;
    }

    public void setCityId(java.lang.Long cityId) {
        this.cityId = cityId;
    }

    public java.lang.String getCityName() {
        return cityName;
    }

    public void setCityName(java.lang.String cityName) {
        this.cityName = cityName;
    }

    public java.lang.Long getAreaId() {
        return areaId;
    }

    public void setAreaId(java.lang.Long areaId) {
        this.areaId = areaId;
    }

    public java.lang.String getAreaName() {
        return areaName;
    }

    public void setAreaName(java.lang.String areaName) {
        this.areaName = areaName;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getAliasName() {
        return aliasName;
    }

    public void setAliasName(java.lang.String aliasName) {
        this.aliasName = aliasName;
    }

    public java.lang.String getAddress() {
        return address;
    }

    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    public java.lang.String getZipcode() {
        return zipcode;
    }

    public void setZipcode(java.lang.String zipcode) {
        this.zipcode = zipcode;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(java.lang.String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public java.lang.String getAptSegment1() {
        return aptSegment1;
    }

    public void setAptSegment1(java.lang.String aptSegment1) {
        this.aptSegment1 = aptSegment1;
    }

    public java.lang.String getAptSegment2() {
        return aptSegment2;
    }

    public void setAptSegment2(java.lang.String aptSegment2) {
        this.aptSegment2 = aptSegment2;
    }

    public java.lang.String getAptSegment3() {
        return aptSegment3;
    }

    public void setAptSegment3(java.lang.String aptSegment3) {
        this.aptSegment3 = aptSegment3;
    }

    public java.lang.String getAptSeg1Sample() {
        return aptSeg1Sample;
    }

    public void setAptSeg1Sample(java.lang.String aptSeg1Sample) {
        this.aptSeg1Sample = aptSeg1Sample;
    }

    public java.lang.String getAptSeg2Sample() {
        return aptSeg2Sample;
    }

    public void setAptSeg2Sample(java.lang.String aptSeg2Sample) {
        this.aptSeg2Sample = aptSeg2Sample;
    }

    public java.lang.String getAptSeg3Sample() {
        return aptSeg3Sample;
    }

    public void setAptSeg3Sample(java.lang.String aptSeg3Sample) {
        this.aptSeg3Sample = aptSeg3Sample;
    }

    public java.lang.Integer getAptCount() {
        return aptCount;
    }

    public void setAptCount(java.lang.Integer aptCount) {
        this.aptCount = aptCount;
    }

    public java.lang.Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(java.lang.Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public java.lang.Byte getStatus() {
        return status;
    }

    public void setStatus(java.lang.Byte status) {
        this.status = status;
    }

    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

    public java.sql.Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(java.sql.Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }

    public java.lang.Long getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(java.lang.Long requestStatus) {
        this.requestStatus = requestStatus;
    }

    public List<CommunityGeoPointDTO> getGeoPointList() {
        return geoPointList;
    }

    public void setGeoPointList(List<CommunityGeoPointDTO> geoPointList) {
        this.geoPointList = geoPointList;
    }
}
