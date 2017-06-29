package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 组配置项id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>groupType: 组类型</li>
 * <li>groupId: 组id</li>
 * <li>targetId: 目标id</li>
 * <li>targetType: 目标类型</li>
 * <li>operatorUid: 操作人员id</li>
 * <li>updateTime: 更新时间</li>
 * </ul>
 */
public class UniongroupConfiguresDTO {
    private Long id;
    private Integer namespaceId;
    private String groupType;
    private Long groupId;
    private Long targetId;
    private String targetType;
    private Long operatorUid;
    private Timestamp updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
