// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Timestamp;
import java.util.List;

/**
 * <ul>
 * <li>enterpriseId: (必填)公司id</li>
 * <li>enterpriseName: (必填)公司名称</li>
 * </ul>
 */
public class BaseVisitorEnterpriseDTO {
    private Long enterpriseId;
    private String enterpriseName;
    @ItemType(VisitorSysBuilding.class)
    private List<VisitorSysBuilding> buildings;

    public BaseVisitorEnterpriseDTO(Long enterpriseId, String enterpriseName) {
        this.enterpriseId = enterpriseId;
        this.enterpriseName = enterpriseName;
    }

    public BaseVisitorEnterpriseDTO() {
    }

    public List<VisitorSysBuilding> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<VisitorSysBuilding> buildings) {
        this.buildings = buildings;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
