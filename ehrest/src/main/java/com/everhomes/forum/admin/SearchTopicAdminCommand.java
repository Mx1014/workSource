// @formatter:off
package com.everhomes.forum.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>senderPhone: 发送者手机号</li>
 * <li>senderNickName: 发送者昵称关键字</li>
 * <li>keyword: 帖子标题、内容关键字</li>
 * <li>startTime: 帖子创建时间在此时间之后</li>
 * <li>endTime: 帖子创建时间在此时间之前</li>
 * <li>pageAnchor: 本页开始锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchTopicAdminCommand {
    private String senderPhone;
    private String senderNickName;
    private String keyword;
    private Long startTime;
    private Long endTime;
    private Long pageAnchor;
    private Integer pageSize;
    
    public SearchTopicAdminCommand() {
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderNickName() {
        return senderNickName;
    }

    public void setSenderNickName(String senderNickName) {
        this.senderNickName = senderNickName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
