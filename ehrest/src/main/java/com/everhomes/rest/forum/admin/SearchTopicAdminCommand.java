// @formatter:off
package com.everhomes.rest.forum.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 用户当前所有小区ID</li>
 * <li>senderPhones: 发送者手机号（要写全整个手机号，不分词），可多个</li>
 * <li>senderNickNames: 发送者昵称（要写全整个昵称，不分词），可多个</li>
 * <li>keyword: 帖子标题、内容关键字</li>
 * <li>startTime: 帖子创建时间在此时间之后，从1970年1月1日0时0分到指定时间的毫秒数</li>
 * <li>endTime: 帖子创建时间在此时间之前，从1970年1月1日0时0分到指定时间的毫秒数</li>
 * <li>pageAnchor: 本页开始锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchTopicAdminCommand {
    private Long communityId;
    @ItemType(String.class)
    private List<String> senderPhones;
    @ItemType(String.class)
    private List<String> senderNickNames;
    private String keyword;
    private Long startTime;
    private Long endTime;
    private Long pageAnchor;
    private Integer pageSize;
    
    public SearchTopicAdminCommand() {
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public List<String> getSenderPhones() {
        return senderPhones;
    }

    public void setSenderPhones(List<String> senderPhones) {
        this.senderPhones = senderPhones;
    }

    public List<String> getSenderNickNames() {
        return senderNickNames;
    }

    public void setSenderNickNames(List<String> senderNickNames) {
        this.senderNickNames = senderNickNames;
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
