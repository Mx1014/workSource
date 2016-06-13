// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.acl.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;

public class AclListOrganizationAdministratorsRestResponse extends RestResponseBase {

    private ListOrganizationMemberCommandResponse response;

    public AclListOrganizationAdministratorsRestResponse () {
    }

    public ListOrganizationMemberCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListOrganizationMemberCommandResponse response) {
        this.response = response;
    }
}
