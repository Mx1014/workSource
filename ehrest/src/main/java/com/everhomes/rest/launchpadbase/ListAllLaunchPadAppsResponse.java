package com.everhomes.rest.launchpadbase;

import com.everhomes.rest.module.AppCategoryDTO;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>appCategoryDtos: 应用分类列表，参考{@link AppCategoryDTO}</li>
 * </ul>
 */
public class ListAllLaunchPadAppsResponse {

    private List<AppCategoryDTO> appCategoryDtos;

    public List<AppCategoryDTO> getAppCategoryDtos() {
        return appCategoryDtos;
    }

    public void setAppCategoryDtos(List<AppCategoryDTO> appCategoryDtos) {
        this.appCategoryDtos = appCategoryDtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
