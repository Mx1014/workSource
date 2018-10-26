// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 城市id,更新有此值，创建无此值</li>
 * <li>provinceName: 省</li>
 * <li>cityName: 市</li>
 * <li>iconUri: 图片uri</li>
 * <li>iconUrl: 图片url</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目（为空代表通用配置）</li>
 * <li>orgId: 管理公司id</li>
 * </ul>
 */
public class CreateOrUpdateCityCommand {
    private Long id;
    private String provinceName;
    private String cityName;
    private String iconUri;
    private String iconUrl;

//  标准版兼容通用
    private String ownerType;
    private Long ownerId;
    private Long orgId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
