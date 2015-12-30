package com.everhomes.rest.recommend;

import java.sql.Timestamp;

/**
 * 
 * @author janson
 *
 */
public class RecommendConfigDTO {
    private Long     id;
    private Long     appid;
    private Integer  suggestType;
    private Integer  sourceType;
    private Long     sourceId;
    private Integer  targetType;
    private Long     targetId;
    private Integer  periodType;
    private Integer  periodValue;
    private Integer  status;
    private Timestamp createTime;
    private Timestamp startTime;
    private Timestamp runningTime;
    private Timestamp expireTime;
    private String   embeddedJson;
    private String   description;
    
    public java.lang.Long getId() {
        return id;
    }
    public void setId(java.lang.Long id) {
        this.id = id;
    }
    public java.lang.Long getAppid() {
        return appid;
    }
    public void setAppid(java.lang.Long appid) {
        this.appid = appid;
    }
    public java.lang.Integer getSuggestType() {
        return suggestType;
    }
    public void setSuggestType(java.lang.Integer suggestType) {
        this.suggestType = suggestType;
    }
    public java.lang.Integer getSourceType() {
        return sourceType;
    }
    public void setSourceType(java.lang.Integer sourceType) {
        this.sourceType = sourceType;
    }
    public java.lang.Long getSourceId() {
        return sourceId;
    }
    public void setSourceId(java.lang.Long sourceId) {
        this.sourceId = sourceId;
    }
    public java.lang.Integer getTargetType() {
        return targetType;
    }
    public void setTargetType(java.lang.Integer targetType) {
        this.targetType = targetType;
    }
    public java.lang.Long getTargetId() {
        return targetId;
    }
    public void setTargetId(java.lang.Long targetId) {
        this.targetId = targetId;
    }
    public java.lang.Integer getPeriodType() {
        return periodType;
    }
    public void setPeriodType(java.lang.Integer periodType) {
        this.periodType = periodType;
    }
    public java.lang.Integer getPeriodValue() {
        return periodValue;
    }
    public void setPeriodValue(java.lang.Integer periodValue) {
        this.periodValue = periodValue;
    }
    public java.lang.Integer getStatus() {
        return status;
    }
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }
    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }
    public java.sql.Timestamp getStartTime() {
        return startTime;
    }
    public void setStartTime(java.sql.Timestamp startTime) {
        this.startTime = startTime;
    }
    public java.sql.Timestamp getRunningTime() {
        return runningTime;
    }
    public void setRunningTime(java.sql.Timestamp runningTime) {
        this.runningTime = runningTime;
    }
    public java.sql.Timestamp getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(java.sql.Timestamp expireTime) {
        this.expireTime = expireTime;
    }
    public java.lang.String getEmbeddedJson() {
        return embeddedJson;
    }
    public void setEmbeddedJson(java.lang.String embeddedJson) {
        this.embeddedJson = embeddedJson;
    }
    public java.lang.String getDescription() {
        return description;
    }
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    
}
