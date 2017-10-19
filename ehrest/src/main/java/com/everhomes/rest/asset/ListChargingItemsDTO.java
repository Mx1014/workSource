//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>chargingItemId: 收费项目id</li>
 * <li>chargingItemName: 收费项目名称</li>
 * <li>projectChargingItemName: 园区收费项目名称</li>
 * <li>isSelected: 是否被选中，1:是;0:否;</li>
 *</ul>
 */
public class ListChargingItemsDTO {
    private Long chargingItemId;
    private String chargingItemName;
    private String projectChargingItemName;
    private Byte isSelected;

    public ListChargingItemsDTO() {
    }

    public String getChargingItemName() {
        return chargingItemName;
    }

    public void setChargingItemName(String chargingItemName) {
        this.chargingItemName = chargingItemName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getChargingItemId() {
        return chargingItemId;
    }

    public void setChargingItemId(Long chargingItemId) {
        this.chargingItemId = chargingItemId;
    }

    public String getProjectChargingItemName() {
        return projectChargingItemName;
    }

    public void setProjectChargingItemName(String projectChargingItemName) {
        this.projectChargingItemName = projectChargingItemName;
    }

    public Byte getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Byte isSelected) {
        this.isSelected = isSelected;
    }

}
