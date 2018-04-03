package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>name: 门牌名</li>
 *     <li>buildingName: 楼栋名</li>
 *     <li>livingStatus: 门牌状态 参考{@link com.everhomes.rest.organization.pm.AddressMappingStatus}</li>
 *     <li>chargeArea: 收费面积</li>
 *     <li>orientation: 朝向</li>
 * </ul>
 * Created by ying.xiong on 2017/8/18.
 */
public class ApartmentAbstractDTO {

    private Long id;

    private String name;

    private String buildingName;

    private Byte livingStatus;

    private Double chargeArea;

    private String orientation;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Double getChargeArea() {
        return chargeArea;
    }

    public void setChargeArea(Double chargeArea) {
        this.chargeArea = chargeArea;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(Byte livingStatus) {
        this.livingStatus = livingStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
