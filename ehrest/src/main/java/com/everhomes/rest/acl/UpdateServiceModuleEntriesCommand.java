package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>moduleId: moduleId</li>
 *     <li>dtos: 里面的id和mdouleId可以不传 {@link com.everhomes.rest.acl.ServiceModuleEntryDTO}</li>
 * </ul>
 */
public class UpdateServiceModuleEntriesCommand {

    private Long moduleId;

    private List<ServiceModuleEntryDTO> dtos;


    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

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
