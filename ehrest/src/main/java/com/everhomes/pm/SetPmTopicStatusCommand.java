// @formatter:off
package com.everhomes.pm;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>topicId: 帖子Id</li>
 * <li>status: 任务状态，0-未处理，1-处理中，2-已处理，参考{@link com.everhomes.pm.PmTaskStatus}</li>
 * </ul>
 */
public class SetPmTopicStatusCommand {
    private long topicId;
    private Byte status;
    
    public SetPmTopicStatusCommand() {
    }
    
    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
