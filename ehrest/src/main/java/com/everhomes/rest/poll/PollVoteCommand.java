// @formatter:off
package com.everhomes.rest.poll;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>pollId: pollId</li>
 *     <li>uuid: uuid</li>
 *     <li>itemIds: itemIds</li>
 * </ul>
 */
public class PollVoteCommand {
    @NotNull
    private Long pollId;
    private String uuid;

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
        if (itemIds == null) {
            itemIds = new ArrayList<Long>();
        }
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
