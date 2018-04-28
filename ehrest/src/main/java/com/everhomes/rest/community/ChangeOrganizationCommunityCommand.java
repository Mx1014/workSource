// @formatter:off
package com.everhomes.rest.community;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: 园区Id</li>
 *     <li>fromOrgId: 原属管理公司Id</li>
 *     <li>toOrgId: 新的管理公司Id</li>
 *     <li>keepAuthorizationFlag: 保留并转移当前项目的园区运营应用授权 参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ChangeOrganizationCommunityCommand {

    private Long communityId;
    private Long fromOrgId;
    private Long toOrgId;
    private Byte keepAuthorizationFlag;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getFromOrgId() {
        return fromOrgId;
    }

    public void setFromOrgId(Long fromOrgId) {
        this.fromOrgId = fromOrgId;
    }

    public Long getToOrgId() {
        return toOrgId;
    }

    public void setToOrgId(Long toOrgId) {
        this.toOrgId = toOrgId;
    }

    public Byte getKeepAuthorizationFlag() {
        return keepAuthorizationFlag;
    }

    public void setKeepAuthorizationFlag(Byte keepAuthorizationFlag) {
        this.keepAuthorizationFlag = keepAuthorizationFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
