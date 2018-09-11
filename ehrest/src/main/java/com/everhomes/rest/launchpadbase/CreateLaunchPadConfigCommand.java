// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>ownerType: 1-园区，4-公司。参考{@link com.everhomes.rest.common.OwnerType}</li>
 *     <li>ownerId: ownerId</li>
 *     <li>navigatorAllIconUri: 容器组件“全部”按钮的IconUri</li>
 * </ul>
 */
public class CreateLaunchPadConfigCommand {

    private Byte ownerType;
    private Long ownerId;
    private String navigatorAllIconUri;

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getNavigatorAllIconUri() {
        return navigatorAllIconUri;
    }

    public void setNavigatorAllIconUri(String navigatorAllIconUri) {
        this.navigatorAllIconUri = navigatorAllIconUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
