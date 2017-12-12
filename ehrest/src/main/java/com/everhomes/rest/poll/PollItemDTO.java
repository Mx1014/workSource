// @formatter:off
package com.everhomes.rest.poll;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>itemId: 选项ID</li>
 *     <li>subject: 主题</li>
 *     <li>coverUrl: 选项路径</li>
 *     <li>voteCount: 选项投票统计</li>
 *     <li>createTime: 创建时间</li>
 *     <li>pollVoterStatus: 投票状态，当前用户是否已投此项 ,1 未投票，2 已投票， 参考{@link PollVoterStatus}</li>
 * </ul>
 */
public class PollItemDTO {
    private Long itemId;
    private String subject;
    private String coverUrl;
    private Integer voteCount;
    private String createTime;
    private Integer pollVoterStatus;


    public PollItemDTO() {
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getPollVoterStatus() {
        return pollVoterStatus;
    }

    public void setPollVoterStatus(Integer pollVoterStatus) {
        this.pollVoterStatus = pollVoterStatus;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
