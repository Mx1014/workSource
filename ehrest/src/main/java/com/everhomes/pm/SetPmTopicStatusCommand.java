// @formatter:off
package com.everhomes.pm;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>topicId: 帖子Id</li>
 * <li>status: 任务状态，0-未处理，1-处理中，2-已处理，参考{@link com.everhomes.pm.PmTaskStatus}</li>
 * </ul>
 */
public class SetPmTopicStatusCommand {
    @ItemType(Long.class)
    private List<Long> topicIds;
    private Byte status;
    
    public SetPmTopicStatusCommand() {
    }

    public List<Long> getTopicIds() {
        return topicIds;
    }

    public void setTopicIds(List<Long> topicIds) {
        this.topicIds = topicIds;
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
