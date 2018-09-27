package com.everhomes.rest.acl;

import com.everhomes.rest.servicemoduleapp.ServiceModuleAppAuthorizationDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos</li>
 *     <li>nextPageAnchor: nextPageAnchor</li>
 * </ul>
 */
public class ListAppAuthorizationsByOrganizatioinIdResponse {
    private List<ServiceModuleAppAuthorizationDTO> dtos;
    private Long nextPageAnchor;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

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
