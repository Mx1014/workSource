// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.rest.acl.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.acl.admin.RoleDTO;

public class AclListAclRoleByOrganizationIdsRestResponse extends RestResponseBase {

    private List<RoleDTO> response;

    public AclListAclRoleByOrganizationIdsRestResponse () {
    }

    public List<RoleDTO> getResponse() {
        return response;
    }

    public void setResponse(List<RoleDTO> response) {
        this.response = response;
    }
}
