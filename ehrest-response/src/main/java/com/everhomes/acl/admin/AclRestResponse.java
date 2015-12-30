// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.acl.admin;

import com.everhomes.rest.acl.admin.ListUserRolesAdminCommandResponse;
import com.everhomes.rest.RestResponseBase;

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
