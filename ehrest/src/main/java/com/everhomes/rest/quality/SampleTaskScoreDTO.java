package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: 例行检查id</li>
 *     <li>ownerId: 例行检查所属owner id</li>
 *     <li>ownerType: 例行检查所属owner类型例如PM</li>
 *     <li>name: 检查名称</li>
 *     <li>startTime: 开始时间</li>
 *     <li>endTime: 结束时间</li>
 *     <li>communityCount: 关联小区数量</li>
 *     <li>averageScore: 平均得分</li>
 *     <li>highestScore: 最高得分</li>
 *     <li>lowestScore: 最低得分</li>
 *     <li>creatorUid: 创建人id</li>
 *     <li>creatorName: 创建人姓名</li>
 * </ul>
 * Created by ying.xiong on 2017/6/2.
 */
public class SampleTaskScoreDTO {

    private Long id;

    @NotNull
    private Long ownerId;

    @NotNull
    private String ownerType;

    private String name;

    private Timestamp startTime;

    private Timestamp endTime;

    private Integer communityCount;

    private Double averageScore;

    private Double highestScore;

    private Double lowestScore;

    private Long creatorUid;

    private String creatorName;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Double getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Double highestScore) {
        this.highestScore = highestScore;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(Double lowestScore) {
        this.lowestScore = lowestScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
