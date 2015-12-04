// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.organization.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.organization.pm.UpdateOrganizationMemberByIdsCommand;

public class OrgRestResponse extends RestResponseBase {

    private UpdateOrganizationMemberByIdsCommand response;

    public OrgRestResponse () {
    }

    public UpdateOrganizationMemberByIdsCommand getResponse() {
        return response;
    }

    public void setResponse(UpdateOrganizationMemberByIdsCommand response) {
        this.response = response;
    }
}
