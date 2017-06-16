package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>flowName: flowName</li>
 *     <li>flowMainId: flowMainId</li>
 *     <li>ownerType: ownerType</li>
 *     <li>endNode: endNode</li>
 *     <li>moduleType: moduleType</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>stopTime: stopTime</li>
 *     <li>status: status</li>
 *     <li>lastNode: lastNode</li>
 *     <li>flowVersion: flowVersion</li>
 *     <li>runTime: runTime</li>
 *     <li>createTime: createTime</li>
 *     <li>updateTime: updateTime</li>
 *     <li>moduleId: moduleId</li>
 *     <li>startNode: startNode</li>
 *     <li>ownerId: ownerId</li>
 *     <li>projectId: projectId</li>
 *     <li>projectType: projectType</li>
 *     <li>stringTag1: stringTag1</li>
 * </ul>
 */
public class FlowDTO {
    private Long id;
    private String flowName;
    private Long flowMainId;
    private String ownerType;
    private Long endNode;
    private String moduleType;
    private Integer namespaceId;
    private Timestamp stopTime;
    private Byte status;
    private Long lastNode;
    private Integer flowVersion;
    private Timestamp runTime;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Long moduleId;
    private Long startNode;
    private Long ownerId;
    private Long projectId;
    private String projectType;

    private String stringTag1;

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public Long getFlowMainId() {
        return flowMainId;
    }

    public void setFlowMainId(Long flowMainId) {
        this.flowMainId = flowMainId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getEndNode() {
        return endNode;
    }

    public void setEndNode(Long endNode) {
        this.endNode = endNode;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

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

    public Timestamp getStopTime() {
        return stopTime;
    }

    public void setStopTime(Timestamp stopTime) {
        this.stopTime = stopTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getLastNode() {
        return lastNode;
    }

    public void setLastNode(Long lastNode) {
        this.lastNode = lastNode;
    }

    public Integer getFlowVersion() {
        return flowVersion;
    }

    public void setFlowVersion(Integer flowVersion) {
        this.flowVersion = flowVersion;
    }

    public Timestamp getRunTime() {
        return runTime;
    }

    public void setRunTime(Timestamp runTime) {
        this.runTime = runTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getStartNode() {
        return startNode;
    }

    public void setStartNode(Long startNode) {
        this.startNode = startNode;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
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

    public String getStringTag1() {
        return stringTag1;
    }

    public void setStringTag1(String stringTag1) {
        this.stringTag1 = stringTag1;
    }
}

