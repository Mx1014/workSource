// @formatter:off
package com.everhomes.rest.poll;

import com.everhomes.util.StringHelper;

public class PollShowResultCommand {
    private Long pollId;
    //poll uuid
    private String uuid;
    
    public PollShowResultCommand() {
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }
    
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
