// @formatter:off
package com.everhomes.rest.officecubicle.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>provinceName: 省</li>
 * <li>cityName: 市</li>
 * <li>iconUri: 图片uri</li>
 * <li>iconUrl: 图片url</li>
 * <li>defaultOrder: 顺序</li>
 * <li>selectFlag: 是否被选中，1选中，0未选中</li>
 * </ul>
 */
public class CityDTO {
    private Long id;
    private String provinceName;
    private String cityName;
    private String iconUri;
    private String iconUrl;
    private Long defaultOrder;
    private Byte selectFlag;

    public Byte getSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(Byte selectFlag) {
        this.selectFlag = selectFlag;
    }

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

    public Long getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Long defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
