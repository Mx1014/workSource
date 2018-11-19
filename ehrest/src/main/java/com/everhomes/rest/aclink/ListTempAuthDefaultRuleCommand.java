// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul> 添加临时授权优先门禁。
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type：0园区 1企业</li>
 * </ul>
 *
 */
public class ListTempAuthDefaultRuleCommand {

    private Long ownerId;
    private Byte ownerType;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
