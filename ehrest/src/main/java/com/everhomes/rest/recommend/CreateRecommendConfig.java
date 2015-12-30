package com.everhomes.rest.recommend;

public class CreateRecommendConfig {
    private Long     appid;
    private Integer  suggestType;
    private Integer  sourceType;
    private Integer sourceId;
    private Integer  targetType;
    private Long     targetId;
    private Integer  periodType;
    private Integer  periodValue;
    private String startTime;
    private String expireTime;
    private String   embeddedJson;
    private String   description;
    
    public Long getAppid() {
        return appid;
    }
    public void setAppid(Long appid) {
        this.appid = appid;
    }
    public Integer getSuggestType() {
        return suggestType;
    }
    public void setSuggestType(Integer suggestType) {
        this.suggestType = suggestType;
    }
    public Integer getSourceType() {
        return sourceType;
    }
    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }
    public Integer getTargetType() {
        return targetType;
    }
    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }
    public Long getTargetId() {
        return targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    public Integer getPeriodType() {
        return periodType;
    }
    public void setPeriodType(Integer periodType) {
        this.periodType = periodType;
    }
    public Integer getPeriodValue() {
        return periodValue;
    }
    public void setPeriodValue(Integer periodValue) {
        this.periodValue = periodValue;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
    public String getEmbeddedJson() {
        return embeddedJson;
    }
    public void setEmbeddedJson(String embeddedJson) {
        this.embeddedJson = embeddedJson;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getSourceId() {
        return sourceId;
    }
    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }
}
