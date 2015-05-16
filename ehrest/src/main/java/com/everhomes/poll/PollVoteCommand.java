// @formatter:off
package com.everhomes.poll;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class PollVoteCommand {
    @NotNull
    private Long pollId;
    
    @ItemType(Long.class)
    private List<Long> itemIds;
    
    public PollVoteCommand() {
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    
    public List<Long> getItemIds() {
        if(itemIds==null){
            itemIds=new ArrayList<Long>();
        }
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
