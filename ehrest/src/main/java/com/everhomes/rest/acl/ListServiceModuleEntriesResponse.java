package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>dtos: dtos {@link com.everhomes.rest.acl.ServiceModuleEntryDTO}</li>
 * </ul>
 */
public class ListServiceModuleEntriesResponse {
    private List<ServiceModuleEntryDTO> dtos;

    public List<ServiceModuleEntryDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<ServiceModuleEntryDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
