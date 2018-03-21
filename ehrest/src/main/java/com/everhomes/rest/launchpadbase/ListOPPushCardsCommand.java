// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>communityId: 小区id</li>
 *     <li>orgId: 公司id</li>
 *     <li>layoutId: layoutId</li>
 *     <li>instanceConfig: 从layout的group中获取，其中包含了组件信息</li>
 * </ul>
 */
public class ListOPPushCardsCommand {

    @NotNull
    private Long communityId;

    private Long orgId;

    private Long layoutId;

    private String instanceConfig;

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

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getInstanceConfig() {
        return instanceConfig;
    }

    public void setInstanceConfig(String instanceConfig) {
        this.instanceConfig = instanceConfig;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
