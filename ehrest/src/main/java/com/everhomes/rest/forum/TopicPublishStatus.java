// @formatter:off
package com.everhomes.rest.forum;

/**
 * <p>帖子内容类型</p>
 * <ul>
 * <li>ALL("all"): 全部</li>
 * <li>UNPUBLISHED("unpublished"): 未发布</li>
 * <li>PUBLISHED("published"): 已发布，未过期</li>
 * <li>EXPIRED("expired"): 已过期</li>
 * </ul>
 */
public enum TopicPublishStatus {
    ALL("all"), UNPUBLISHED("unpublished"), PUBLISHED("published"), EXPIRED("expired");
    
    private String code;
    
    private TopicPublishStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static TopicPublishStatus fromCode(String code) {
        if(code != null) {
            TopicPublishStatus[] values = TopicPublishStatus.values();
            for(TopicPublishStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
