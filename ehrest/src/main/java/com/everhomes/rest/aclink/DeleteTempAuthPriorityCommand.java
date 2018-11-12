// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>id: 所属上级的id</li>
 * </ul>
 */
public class DeleteTempAuthPriorityCommand {
    @NotNull
    private Long id;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
