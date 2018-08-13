package com.everhomes.rest.filemanagement;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>返回值:
 * <li>ownerId: ownerId</li>
 * </ul>
 */
public class ListAllFlodersCommand {
    private Long ownerId;

    public ListAllFlodersCommand(){

    }
    public ListAllFlodersCommand(Long ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
