// @formatter:off
package com.everhomes.organization.pm;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>topicId: 帖子Id</li>
 * <li>status: 任务状态，0-未处理，1-处理中，2-已处理，参考{@link com.everhomes.pm.PmTaskStatus}</li>
 * <li>communityId: 小区Id</li>
 * </ul>
 */
public class SetPmTopicStatusCommand {
	private Long topicId;
    private Byte status;
    private Long communityId;
    
    public SetPmTopicStatusCommand() {
    }
    
    public Long getTopicId() {
		return topicId;
	}


	public void setTopicId(Long topicId) {
		this.topicId = topicId; 
	}


	public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
