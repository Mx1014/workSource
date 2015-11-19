// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.acl.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.acl.Role;

public class AclListAclRolesRestResponse extends RestResponseBase {

    private List<Role> response;

    public AclListAclRolesRestResponse () {
    }

    public List<Role> getResponse() {
        return response;
    }

    public void setResponse(List<Role> response) {
        this.response = response;
    }
}
