// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: Id</li>
 *     <li>organizationId: 默认配置时使用organizationId</li>
 *     <li>communityId: 园区独立配置时使用communityId</li>
 *     <li>name: 名字</li>
 *     <li>bizUrl: bizUrl</li>
 *     <li>logoUri: logoUri</li>
 *     <li>logoUrl: logoUrl</li>
 *     <li>status: 状态0-删除，1-未启用，2-启用，参考{@link com.everhomes.rest.launchpadbase.CommunityBizStatus}</li>
 * </ul>
 */
public class CommunityBizDTO {

    private Long id;
    private Long organizationId;
    private Long communityId;
    private String name;
    private String bizUrl;
    private String logoUri;
    private String logoUrl;
    private Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBizUrl() {
        return bizUrl;
    }

    public void setBizUrl(String bizUrl) {
        this.bizUrl = bizUrl;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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
