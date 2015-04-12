package com.everhomes.forum;

public class SearchTopicCommand {
    private Double longitude;
    private Double latitude;
    
    // json encoded condition string
    private String conditionJson;
    
    private Byte globalFlag;
    private Long pageOffset;
    private Long pageSize;
    
    public SearchTopicCommand() {
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getConditionJson() {
        return conditionJson;
    }

    public void setConditionJson(String conditionJson) {
        this.conditionJson = conditionJson;
    }

    public Byte getGlobalFlag() {
        return globalFlag;
    }

    public void setGlobalFlag(Byte globalFlag) {
        this.globalFlag = globalFlag;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
