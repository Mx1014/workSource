// @formatter:off
package com.everhomes.rest.community.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>isAuditing: 是否有权限审核，请参考{@link com.everhomes.rest.community.admin.CheckAuditingType}</li>
 * </ul>
 */
public class CheckUserAuditingAdminResponse {

    private Byte isAuditing;

    public Byte getIsAuditing() {
        return isAuditing;
    }

    public void setIsAuditing(Byte isAuditing) {
        this.isAuditing = isAuditing;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
