package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *  <li>ownerId: 任务所属组织等的id</li>
 *  <li>ownerType: 任务所属组织类型，如enterprise</li>
 *  <li>targetName: 任务所属项目名</li>
 *  <li>targetId: 任务所属项目id</li>
 *  <li>targetType: 任务所属项目类型</li>
 *  <li>startDate: 任务开始日期</li>
 *  <li>endDate: 任务结束日期</li>
 *  <li>sampleId: 例行检查id</li>
 *  <li>executorId: 执行人id</li>
 *  <li>executorName: 执行人名称</li>
 *  <li>executeStatus: 执行状态</li>
 *  <li>reviewStatus: 审阅状态</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>manualFlag: 是否手动添加 0：自动生成，1：手动添加，2：例行检查生成的任务</li>
 * </ul>
 * Created by ying.xiong on 2017/6/8.
 */
public class SearchQualityTasksCommand {

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private String targetName;

    private Long targetId;

    private String targetType;

    private Long startDate;

    private Long endDate;

    private Long sampleId;

    private Byte executeStatus;

    private Long executorId;

    private String executorName;

    private Byte reviewResult;

    private Byte manualFlag;

    private Long pageAnchor;

    private Integer pageSize;

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Byte getExecuteStatus() {
        return executeStatus;
    }

    public void setExecuteStatus(Byte executeStatus) {
        this.executeStatus = executeStatus;
    }

    public Byte getManualFlag() {
        return manualFlag;
    }

    public void setManualFlag(Byte manualFlag) {
        this.manualFlag = manualFlag;
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

    public Byte getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(Byte reviewResult) {
        this.reviewResult = reviewResult;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
