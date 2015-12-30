package com.everhomes.rest.group;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>groupId: The Id group Group </li>
 * </ul>
 * @author janson
 *
 */
public class DeleteGroupByIdCommand {
    @NotNull
    private Long groupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    
}
