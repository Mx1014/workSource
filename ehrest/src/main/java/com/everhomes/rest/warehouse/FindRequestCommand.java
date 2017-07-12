package com.everhomes.rest.warehouse;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>requestId: 申请id</li>
 *     <li>ownerType: 所属类型 eg：EhOrganizations</li>
 *     <li>ownerId: 所属类型id</li>
 * </ul>
 * Created by ying.xiong on 2017/5/11.
 */
public class FindRequestCommand {

    private String ownerType;

    private Long ownerId;

    private Long requestId;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
