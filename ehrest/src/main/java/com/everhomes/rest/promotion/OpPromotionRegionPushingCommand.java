package com.everhomes.rest.promotion;

/**
 * <ul>
 * <li>startTime: 活动开始时间</li>
 * <li>endTime: 活动结束时间</li>
 * <li>content: 推送内容</li>
 * <li>namespaceId: 域空间</li>
 * <li>scopeCode: 城市或小区类型</li>
 * <li>scopeId: ID </li>
 * </ul>
 * @author janson
 *
 */
public class OpPromotionRegionPushingCommand {
    private Byte     scopeCode;
    private Long     scopeId;
    private String content;
    private Integer namespaceId;
    private Long startTime;
    private Long     endTime;
    
    public Byte getScopeCode() {
        return scopeCode;
    }
    public void setScopeCode(Byte scopeCode) {
        this.scopeCode = scopeCode;
    }
    public Long getScopeId() {
        return scopeId;
    }
    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public Long getStartTime() {
        return startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }    
}
