package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>某日某场所预定状态
 * <li>siteId：场所id</li>
 * <li>ruleDate：日期</li>
 * <li>packageName:套餐名</li>
 * <li>sceneType: 场景标识，用一个标识代替原来用多个字段共同表示的标识</li>
 * <li>rentalType: 价格类型，参考{@link com.everhomes.rest.rentalv2.RentalType}</li>
 * <li>priceType: 0 按时长定价 1 起步价模式</li>
 * </ul>
 */
public class FindRentalSiteDayStatusCommand {

    private String resourceType;
    @NotNull
    private Long siteId;
    @NotNull
    private Long ruleDate;

    private Byte rentalType;
    private Byte priceType;
    private String packageName;
    private String sceneType;
    private String sceneToken;

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getRuleDate() {
        return ruleDate;
    }

    public void setRuleDate(Long ruleDate) {
        this.ruleDate = ruleDate;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Byte getPriceType() {
        return priceType;
    }

    public void setPriceType(Byte priceType) {
        this.priceType = priceType;
    }

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

}
