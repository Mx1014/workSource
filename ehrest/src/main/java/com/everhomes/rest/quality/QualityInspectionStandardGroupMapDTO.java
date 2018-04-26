package com.everhomes.rest.quality;

import java.sql.Timestamp;

/**
 * Created by Rui.Jia  2018/3/2 18 :57
 */

public class QualityInspectionStandardGroupMapDTO {

    private Long id;
    private Byte groupType;
    private Long standardId;
    private Long groupId;
    private Long inspectorUid;
    private Timestamp createTime;
    private Long positionId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getGroupType() {
        return groupType;
    }

    public void setGroupType(Byte groupType) {
        this.groupType = groupType;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getInspectorUid() {
        return inspectorUid;
    }

    public void setInspectorUid(Long inspectorUid) {
        this.inspectorUid = inspectorUid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }
}
