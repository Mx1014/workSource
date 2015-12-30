// @formatter:off
package com.everhomes.rest.fleamarket;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class FleaMarketUpdateCommand {
    @NotNull
    private Long forumId;
    
    @NotNull
    private Long topicId;
    
    private Integer barterFlag;
    private String price;
    private Integer closeFlag;

    public FleaMarketUpdateCommand() {
    }
    
    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Integer getBarterFlag() {
        return barterFlag;
    }

    public void setBarterFlag(Integer barterFlag) {
        this.barterFlag = barterFlag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(Integer closeFlag) {
        this.closeFlag = closeFlag;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
