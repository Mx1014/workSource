package com.everhomes.rest.module;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>appType: appType，参考{@link ServiceModuleAppType}</li>
 * </ul>
 */
public class ListAppCategoryResponse {

    private List<AppCategoryDTO> dtos;

    public List<AppCategoryDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<AppCategoryDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
