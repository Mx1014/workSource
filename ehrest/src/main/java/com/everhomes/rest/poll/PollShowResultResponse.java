// @formatter:off
package com.everhomes.rest.poll;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>poll: poll {@link com.everhomes.rest.poll.PollDTO}</li>
 *     <li>items: items {@link com.everhomes.rest.poll.PollItemDTO}</li>
 * </ul>
 */
public class PollShowResultResponse {
    private PollDTO poll;

    @ItemType(PollItemDTO.class)
    private List<PollItemDTO> items;

    public PollShowResultResponse() {
    }

    public PollDTO getPoll() {
        return poll;
    }

    public void setPoll(PollDTO poll) {
        this.poll = poll;
    }

    public List<PollItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PollItemDTO> items) {
        this.items = items;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
