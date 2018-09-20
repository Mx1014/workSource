package com.everhomes.rest.enterprise;

import com.everhomes.util.StringHelper;

public class WholeAddressComamnd {

    private Long organizationId;

    private String siteName;

    private Long communityId;

    private String wholeAddressNameOld;

    private String wholeAddressNameNew;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getWholeAddressNameOld() {
        return wholeAddressNameOld;
    }

    public void setWholeAddressNameOld(String wholeAddressNameOld) {
        this.wholeAddressNameOld = wholeAddressNameOld;
    }

    public String getWholeAddressNameNew() {
        return wholeAddressNameNew;
    }

    public void setWholeAddressNameNew(String wholeAddressNameNew) {
        this.wholeAddressNameNew = wholeAddressNameNew;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
