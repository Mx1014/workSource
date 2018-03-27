// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>描述: 此为上下文信息，客户端和服务器根据具体业务传具体参数。例如：工作台获取应用客户端和服务器约定传orgId，广场获取应用客户端和服务器约定传communityId</li>
 *     <li>communityId: 小区id</li>
 *     <li>orgId: 公司id</li>
 * </ul>
 */
public class ContentDTO {

    @NotNull
    private Long communityId;

    private Long orgId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
