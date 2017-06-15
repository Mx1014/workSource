package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>ownerId: 例行检查所属owner id</li>
 *     <li>ownerType: 例行检查所属owner类型例如PM</li>
 *     <li>communityId: 小区id</li>
 *     <li>sampleId: 例行检查id</li>
 *     <li>communityName: 项目名称</li>
 *     <li>executorName: 执行人姓名</li>
 *     <li>executeStatus: 执行状态 参考{@link com.everhomes.rest.quality.QualityInspectionTaskStatus}</li>
 *      <li>pageAnchor: 锚点</li>
 *      <li>pageSize: 页面大小</li>
 * </ul>
 * Created by ying.xiong on 2017/6/2.
 */
public class ListSampleQualityInspectionTasksCommand {

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private Long communityId;

    private Long sampleId;

    private String communityName;

    private String executorName;

    private Byte executeStatus;

    private Long pageAnchor;

    private Integer pageSize;

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public Byte getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Byte executeStatus) {
        this.executeStatus = executeStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
