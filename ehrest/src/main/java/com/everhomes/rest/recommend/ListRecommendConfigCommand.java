package com.everhomes.rest.recommend;

public class ListRecommendConfigCommand {
    private Long appId;
    private Integer suggestType;
    private Integer sourceType;
    private Integer targetType;
    private Long sourceId;
    private Long targetId;
    private Long pageAnchor;
    private Integer pageSize;
    
    public Long getAppId() {
        return appId;
    }
    public void setAppId(Long appId) {
        this.appId = appId;
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
    public Long getSourceId() {
        return sourceId;
    }
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
    public Long getTargetId() {
        return targetId;
    }
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
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
    
    
}
