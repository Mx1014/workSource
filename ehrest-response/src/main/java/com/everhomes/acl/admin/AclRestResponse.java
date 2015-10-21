// @formatter:off
// generated at 2015-10-21 17:44:17
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
