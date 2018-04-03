package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: 组配置项id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>groupType: 组类型</li>
 * <li>groupId: 组id</li>
 * <li>currentId: 目标id</li>
 * <li>currentType: 目标类型</li>
 * <li>currentName: 目标名称</li>
 * <li>operatorUid: 操作人员id</li>
 * <li>updateTime: 更新时间</li>
 * </ul>
 */
public class UniongroupConfiguresDTO {
    private Long id;
    private Integer namespaceId;
    private String groupType;
    private Long groupId;
    private Long currentId;
    private String currentType;
    private String currentName;
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

    public Long getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Long currentId) {
        this.currentId = currentId;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
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

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
