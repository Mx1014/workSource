// @formatter:off
// generated at 2015-11-10 11:23:24
package com.everhomes.acl.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.acl.admin.ListUserRolesAdminCommandResponse;

public class AclRestResponse extends RestResponseBase {

    private ListUserRolesAdminCommandResponse response;

    public AclRestResponse () {
    }

    public ListUserRolesAdminCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListUserRolesAdminCommandResponse response) {
        this.response = response;
    }
}
