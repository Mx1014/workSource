package com.everhomes.rest.launchad;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>contentType: 广告类型 {@link com.everhomes.rest.launchad.LaunchAdType}</li>
 *     <li>contentUriOrigin: 广告文件uri</li>
 *     <li>timesPerDay: 每天显示次数, 为 0 则不限制</li>
 *     <li>displayInterval: 最小的显示间隔时间, 单位为分钟, 为 0 则不限制</li>
 *     <li>durationTime: 显示时间, 单位 秒</li>
 *     <li>skipFlag: 是否支持跳过 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 *     <li>targetType: 跳转类型 {@link com.everhomes.rest.banner.BannerTargetType}</li>
 *     <li>targetData: 跳转类型对应的data,每种targetType对应的data都不一样,将targetData对象转换成json字符串的形式</li>
 *     <li>resourceName: 文件名称</li>
 *     <li>status: 状态 {@link com.everhomes.rest.launchad.LaunchAdStatus}</li>
 *     <li>appId: appId</li>
 *     <li>currentOrgId: 物业公司id</li>
 * </ul>
 */
public class CreateOrUpdateLaunchAdCommand {

    private Long id;
    @NotNull
    private Integer namespaceId;
    private String contentType;
    private String contentUriOrigin;
    private Integer timesPerDay;
    private Integer displayInterval;
    private Integer durationTime;
    private Byte skipFlag;

    @NotNull
    private String targetType;
    private String targetData;
    private String resourceName;

    private Byte status;

    private Long appId;
    private Long currentOrgId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentUriOrigin() {
        return contentUriOrigin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContentUriOrigin(String contentUriOrigin) {
        this.contentUriOrigin = contentUriOrigin;
    }

    public Integer getTimesPerDay() {
        return timesPerDay;
    }

    public void setTimesPerDay(Integer timesPerDay) {
        this.timesPerDay = timesPerDay;
    }

    public Integer getDisplayInterval() {
        return displayInterval;
    }

    public void setDisplayInterval(Integer displayInterval) {
        this.displayInterval = displayInterval;
    }

    public Integer getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    public Byte getSkipFlag() {
        return skipFlag;
    }

    public void setSkipFlag(Byte skipFlag) {
        this.skipFlag = skipFlag;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetData() {
        return targetData;
    }

    public void setTargetData(String targetData) {
        this.targetData = targetData;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(Long currentOrgId) {
        this.currentOrgId = currentOrgId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
