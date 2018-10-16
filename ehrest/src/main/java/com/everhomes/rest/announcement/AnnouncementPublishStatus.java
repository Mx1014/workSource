// @formatter:off
package com.everhomes.rest.announcement;

/**
 * <p>帖子内容类型</p>
 * <ul>
 * <li>ALL("all"): 全部</li>
 * <li>UNPUBLISHED("unpublished"): 未发布</li>
 * <li>PUBLISHED("published"): 已发布，未过期</li>
 * <li>EXPIRED("expired"): 已过期</li>
 * <li>PUBLISHING_AND_EXPIRED("publishing_and_expired"): 发布中+已过期</li>
 * </ul>
 */
public enum AnnouncementPublishStatus {
    ALL("all"), UNPUBLISHED("unpublished"), PUBLISHED("published"), EXPIRED("expired"), PUBLISHING_AND_EXPIRED("publishing_and_expired");

    private String code;

    private AnnouncementPublishStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static AnnouncementPublishStatus fromCode(String code) {
        if(code != null) {
            AnnouncementPublishStatus[] values = AnnouncementPublishStatus.values();
            for(AnnouncementPublishStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
