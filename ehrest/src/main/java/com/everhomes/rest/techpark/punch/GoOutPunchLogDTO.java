package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：id</li>
 * <li>organizationId：企业Id 必填</li>
 * <li>latitude: 坐标纬度 必填</li>
 * <li>longitude： 坐标经度 必填</li>
 * <li>locationInfo： 地理位置信息 必填</li>
 * <li>wifiInfo： wifi信息 非必填</li>
 * <li>imgUri：照片uri 必填</li>
 * <li>imgUrl：照片url 必填 </li>
 * <li>description：备注 </li>
 * <li>punchDate：打卡日期 </li>
 * <li>punchTime：打卡时间 </li>
 * <li>ruleName：规则名称 </li>
 * <li>createType： 创建类型 非必填 参考{@link com.everhomes.rest.techpark.punch.CreateType}</li>
 * </ul>
 */
public class GoOutPunchLogDTO {
    private Long id;
    private Long userId;
    private Long organizationId;
    private Integer namespaceId;
    private Double longitude;
    private Double latitude;
    private String locationInfo;
    private String wifiInfo;
    private String imgUri;
    private String imgUrl;
    private String description;
    private Long punchDate;
    private Long punchOrganizationId;
    private Long punchTime;
    private String ruleName;
    private Byte createType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Byte getCreateType() {
        return createType;
    }

    public void setCreateType(Byte createType) {
        this.createType = createType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getPunchDate() {
        return punchDate;
    }

    public void setPunchDate(Long punchDate) {
        this.punchDate = punchDate;
    }

    public Long getPunchOrganizationId() {
        return punchOrganizationId;
    }

    public void setPunchOrganizationId(Long punchOrganizationId) {
        this.punchOrganizationId = punchOrganizationId;
    }

    public Long getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(Long punchTime) {
        this.punchTime = punchTime;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWifiInfo() {
        return wifiInfo;
    }

    public void setWifiInfo(String wifiInfo) {
        this.wifiInfo = wifiInfo;
    }
}
