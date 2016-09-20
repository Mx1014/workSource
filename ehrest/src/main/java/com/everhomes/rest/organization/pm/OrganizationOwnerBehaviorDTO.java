package com.everhomes.rest.organization.pm;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 记录id</li>
 *     <li>building: 楼栋</li>
 *     <li>apartment: 门牌号</li>
 *     <li>behaviorTyp: 服务类别 {@link OrganizationOwnerBehaviorType}</li>
 *     <li>behaviorTime: 发生时间</li>
 * </ul>
 */
public class OrganizationOwnerBehaviorDTO {
    private Long    id;
    private String  building;
    private String  apartment;
    private String  behaviorType;
    private Timestamp behaviorTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType) {
        this.behaviorType = behaviorType;
    }

    public Timestamp getBehaviorTime() {
        return behaviorTime;
    }

    public void setBehaviorTime(Timestamp behaviorTime) {
        this.behaviorTime = behaviorTime;
    }
}
