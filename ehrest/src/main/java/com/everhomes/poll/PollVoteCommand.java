package com.everhomes.poll;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class PollVoteCommand {
    @NotNull
    private Long pollId;
    
    // List<Long>
    private String itemIdsJson;
    
    public PollVoteCommand() {
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public String getItemIdsJson() {
        return itemIdsJson;
    }

    public void setItemIdsJson(String itemIdsJson) {
        this.itemIdsJson = itemIdsJson;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
