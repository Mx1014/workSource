// @formatter:off
package com.everhomes.rest.community;


import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>id: 小区Id</li>
 * <li>requestStatus: 收集状态，参考{@link com.everhomes.rest.community.RequestStatus}}</li>
 * </ul>
 */
public class UpdateCommunityRequestStatusCommand {
    @NotNull
    private Long id;
    private Long requestStatus;
    
    public UpdateCommunityRequestStatusCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Long requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
