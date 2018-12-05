// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * </ul>
 * @author janson
 *
 */
public class SearchDoorServerCommand {
    private Long doorId;

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
}
