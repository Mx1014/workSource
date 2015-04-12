package com.everhomes.forum;

import com.everhomes.util.StringHelper;

public class MakeTopCommand {
    private Long forumId;
    private Long topicId;
    private Byte topFlag;
    
    public MakeTopCommand() {
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

    public Byte getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(Byte topFlag) {
        this.topFlag = topFlag;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
