// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>communityId: communityId</li>
 *     <li>name: name</li>
 *     <li>bizUrl: bizUrl</li>
 *     <li>logoUri: logoUri</li>
 *     <li>status: 状态0-删除，1-未启用，2-启用，参考{@link com.everhomes.rest.launchpadbase.CommunityBizStatus}</li>
 * </ul>
 */
public class CreateCommunityBiz {

    private Long communityId;
    private String name;
    private String bizUrl;
    private String logoUri;
    private Byte status;

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
