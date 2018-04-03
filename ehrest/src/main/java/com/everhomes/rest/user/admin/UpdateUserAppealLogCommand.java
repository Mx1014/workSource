// @formatter:off
package com.everhomes.rest.user.admin;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>status: 状态 {@link com.everhomes.rest.user.admin.UserAppealLogStatus}</li>
 * </ul>
 */
public class UpdateUserAppealLogCommand {

    @NotNull
    private Long id;
    @NotNull
    private Byte status;

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
