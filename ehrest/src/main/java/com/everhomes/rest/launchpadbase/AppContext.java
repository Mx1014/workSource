// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>解释：应用上下文信息。应用往服务器请求时，可以带上当前环境的上下文信息，包括园区、公司和家庭等。</li>
 *     <li>背景：原有很多业务使了sceneToken，而sceneToken仅携带一个entityId，并且标准版里面没有场景的概念没有sceneToken。</li>
 *     <li>处理：为了兼容大多数业务，客户端会伪造一个sceneToken，服务器会尝试使用使用AppContext和SceneTokenDTO解析，并将解析后的内容以AppContext的形式存到UserContext中。</li>
 *     <li>appId: 应用id</li>
 *     <li>communityId: 园区小区Id</li>
 *     <li>organizationId: 公司Id</li>
 *     <li>familyId: 家庭Id</li>
 * </ul>
 */
public class AppContext {

    private Long appId;

    private Long communityId;

    private Long organizationId;

    private Long familyId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
