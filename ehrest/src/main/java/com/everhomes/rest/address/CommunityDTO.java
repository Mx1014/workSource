// @formatter:off
package com.everhomes.rest.address;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.rest.community.CommunityGeoPointDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 小区Id</li>
 *     <li>uuid: 小区uuid，作为唯一标识</li>
 *     <li>provinceId: 省份Id</li>
 *     <li>provinceName: 省份名称</li>
 *     <li>cityId: 城市Id</li>
 *     <li>cityName: 城市名称</li>
 *     <li>areaId: 区域Id</li>
 *     <li>areaName: 区域名称</li>
 *     <li>name: 小区名称</li>
 *     <li>aliasName: 小区别名</li>
 *     <li>address: 小区地址</li>
 *     <li>zipcode: 邮政编码</li>
 *     <li>description: 简略描述</li>
 *     <li>detailDescription: 详细描述</li>
 *     <li>aptSegment1: aptSegment1</li>
 *     <li>aptSegment2: aptSegment2</li>
 *     <li>aptSegment3: aptSegment3</li>
 *     <li>aptSeg1Sample: aptSeg1Sample</li>
 *     <li>aptSeg2Sample: aptSeg2Sample</li>
 *     <li>aptSeg3Sample: aptSeg3Sample</li>
 *     <li>aptCount: 公寓数</li>
 *     <li>creatorUid: 创建者Id</li>
 *     <li>status: 小区状态，参考{@link com.everhomes.rest.address.CommunityAdminStatus}</li>
 *     <li>createTime: 创建时间</li>
 *     <li>deleteTime: 删除时间</li>
 *     <li>requestStatus: 小区收集状态，参考{@link com.everhomes.rest.community.RequestStatus}</li>
 *     <li>communityType: 园区类型，参考{@link com.everhomes.rest.community.CommunityType}</li>
 *     <li>defaultForumId: 默认论坛ID，每个园区都有一个自己的默认论坛用于放园区整体的帖子（如公告）</li>
 *     <li>feedbackForumId: 意见论坛ID，每个园区都有一个自己的意见反馈论坛用于放园区意见反馈帖子</li>
 *     <li>updateTime: 更新时间</li>
 *     <li>areaSize: 面积</li>
 *     <li>sharedArea: 公摊面积</li>
 *     <li>chargeArea: 收费面积</li>
 *     <li>buildArea: 建筑面积</li>
 *     <li>rentArea: 出租面积</li>
 *     <li>categoryName: 分类名称</li>
 *     <li>categoryId: 分类id</li>
 *     <li>communityUserCount: 统计人数</li>
 *     <li>communityNumber: 园区编号</li>
 *     <li>firstLatterOfName: 名字首字母</li>
 *     <li>pmOrgId: 管理公司Id</li>
 *     <li>pmOrgName: 管理公司名称</li>
 *     <li>appSelfConfigFlag: 是否自己配置参考，0,null-否（跟随默认），1-是（自己配置）{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>geoPointList: 小区经纬度列表，参考{@link com.everhomes.rest.community.CommunityGeoPointDTO}</li>
 * </ul>
 */
public class CommunityDTO {
    private Long id;
    private String uuid;
    private Long provinceId;
    private String provinceName;
    private Long cityId;
    private String cityName;
    private Long areaId;
    private String areaName;
    private String name;
    private String aliasName;
    private String address;
    private String zipcode;
    private String description;
    private String detailDescription;
    private String aptSegment1;
    private String aptSegment2;
    private String aptSegment3;
    private String aptSeg1Sample;
    private String aptSeg2Sample;
    private String aptSeg3Sample;
    private Integer aptCount;
    private Long creatorUid;
    private Byte status;
    private Timestamp createTime;
    private Timestamp deleteTime;
    private Long requestStatus;
    private Byte communityType;
    private Long defaultForumId;
    private Long feedbackForumId;
    private Timestamp updateTime;
    private Double areaSize;
    private Double sharedArea;
    private Double chargeArea;
    private Double buildArea;
    private Double rentArea;

    private String categoryName;

    private Long categoryId;

    private Integer communityUserCount;

    private String communityNumber;

    private String firstLatterOfName;

    private Long pmOrgId;
    private String pmOrgName;
    private Byte appSelfConfigFlag;

    @ItemType(CommunityGeoPointDTO.class)
    private List<CommunityGeoPointDTO> geoPointList;

    public CommunityDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public String getAptSegment1() {
        return aptSegment1;
    }

    public void setAptSegment1(String aptSegment1) {
        this.aptSegment1 = aptSegment1;
    }

    public String getAptSegment2() {
        return aptSegment2;
    }

    public void setAptSegment2(String aptSegment2) {
        this.aptSegment2 = aptSegment2;
    }

    public String getAptSegment3() {
        return aptSegment3;
    }

    public void setAptSegment3(String aptSegment3) {
        this.aptSegment3 = aptSegment3;
    }

    public String getAptSeg1Sample() {
        return aptSeg1Sample;
    }

    public void setAptSeg1Sample(String aptSeg1Sample) {
        this.aptSeg1Sample = aptSeg1Sample;
    }

    public String getAptSeg2Sample() {
        return aptSeg2Sample;
    }

    public void setAptSeg2Sample(String aptSeg2Sample) {
        this.aptSeg2Sample = aptSeg2Sample;
    }

    public String getAptSeg3Sample() {
        return aptSeg3Sample;
    }

    public void setAptSeg3Sample(String aptSeg3Sample) {
        this.aptSeg3Sample = aptSeg3Sample;
    }

    public Integer getAptCount() {
        return aptCount;
    }

    public void setAptCount(Integer aptCount) {
        this.aptCount = aptCount;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Timestamp deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Long getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Long requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    public Long getDefaultForumId() {
        return defaultForumId;
    }

    public void setDefaultForumId(Long defaultForumId) {
        this.defaultForumId = defaultForumId;
    }

    public Long getFeedbackForumId() {
        return feedbackForumId;
    }

    public void setFeedbackForumId(Long feedbackForumId) {
        this.feedbackForumId = feedbackForumId;
    }

    public List<CommunityGeoPointDTO> getGeoPointList() {
        return geoPointList;
    }

    public void setGeoPointList(List<CommunityGeoPointDTO> geoPointList) {
        this.geoPointList = geoPointList;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Double areaSize) {
        this.areaSize = areaSize;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getBuildArea() {
        return buildArea;
    }

    public void setBuildArea(Double buildArea) {
        this.buildArea = buildArea;
    }

    public Double getChargeArea() {
        return chargeArea;
    }

    public void setChargeArea(Double chargeArea) {
        this.chargeArea = chargeArea;
    }

    public Double getRentArea() {
        return rentArea;
    }

    public void setRentArea(Double rentArea) {
        this.rentArea = rentArea;
    }

    public Double getSharedArea() {
        return sharedArea;
    }

    public void setSharedArea(Double sharedArea) {
        this.sharedArea = sharedArea;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getCommunityUserCount() {
        return communityUserCount;
    }

    public void setCommunityUserCount(Integer communityUserCount) {
        this.communityUserCount = communityUserCount;
    }

    public String getCommunityNumber() {
        return communityNumber;
    }

    public void setCommunityNumber(String communityNumber) {
        this.communityNumber = communityNumber;
    }

    public String getFirstLatterOfName() {
        return firstLatterOfName;
    }

    public void setFirstLatterOfName(String firstLatterOfName) {
        this.firstLatterOfName = firstLatterOfName;
    }

    public Long getPmOrgId() {
        return pmOrgId;
    }

    public void setPmOrgId(Long pmOrgId) {
        this.pmOrgId = pmOrgId;
    }

    public String getPmOrgName() {
        return pmOrgName;
    }

    public void setPmOrgName(String pmOrgName) {
        this.pmOrgName = pmOrgName;
    }

    public Byte getAppSelfConfigFlag() {
        return appSelfConfigFlag;
    }

    public void setAppSelfConfigFlag(Byte appSelfConfigFlag) {
        this.appSelfConfigFlag = appSelfConfigFlag;
    }
}
