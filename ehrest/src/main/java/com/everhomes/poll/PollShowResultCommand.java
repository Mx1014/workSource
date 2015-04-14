// @formatter:off
package com.everhomes.poll;

import com.everhomes.util.StringHelper;

public class PollShowResultCommand {
    private Long pollId;
    
    public PollShowResultCommand() {
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }
    
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
