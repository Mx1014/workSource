// @formatter:off
package com.everhomes.rest.forum;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 失物招领所在帖子ID</li>
 * <li>status：认领状态，参考{@link com.everhomes.rest.forum.LostAndFoundStatus}</li>
 * </ul>
 */
public class LostAndFoundCommand {
    @NotNull
    private Long id;
    
    private Byte status;

    public LostAndFoundCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
