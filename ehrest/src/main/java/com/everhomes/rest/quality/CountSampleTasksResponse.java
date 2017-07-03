package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: 例行检查id</li>
 *     <li>ownerId: 例行检查所属owner id</li>
 *     <li>ownerType: 例行检查所属owner类型例如PM</li>
 *     <li>name: 检查名称</li>
 *     <li>communityCount: 关联小区数量</li>
 *     <li>averageScore: 平均得分</li>
 *     <li>taskCount: 任务数</li>
 *     <li>correctionRate: 整改率</li>
 * </ul>
 * Created by ying.xiong on 2017/6/2.
 */
public class CountSampleTasksResponse {

    private Long id;

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private String name;

    private Integer communityCount;

    private Integer taskCount;

    private Double averageScore;

    private Double correctionRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Integer getCommunityCount() {
        return communityCount;
    }

    public void setCommunityCount(Integer communityCount) {
        this.communityCount = communityCount;
    }

    public Double getCorrectionRate() {
        return correctionRate;
    }

    public void setCorrectionRate(Double correctionRate) {
        this.correctionRate = correctionRate;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
