package com.everhomes.rest.acl;

import com.everhomes.rest.servicemoduleapp.ServiceModuleAppAuthorizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>ownerId: ownerId</li>
 * </ul>
 */
public class ListAppAuthorizationsByOwnerIdResponse {
    private List<ServiceModuleAppAuthorizationDTO> dtos;

    public List<ServiceModuleAppAuthorizationDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ServiceModuleAppAuthorizationDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }


}
