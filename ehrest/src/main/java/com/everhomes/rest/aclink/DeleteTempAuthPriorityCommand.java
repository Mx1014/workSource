// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>id: 所属上级的id</li>
 * </ul>
 */
public class DeleteTempAuthPriorityCommand {

    private List<Long> id;

    public List<Long> getId() {
        return id;
    }

    public void setId( List<Long> id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
