// @formatter:off
package com.everhomes.rest.launchpad;

import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>name: name</li>
 *     <li>IconUrl: IconUrl</li>
 *     <li>appDtos: 参考{@link  AppDTO}</li>
 * </ul>
 */
public class LaunchPadCategoryDTO {


    private String name;

    private String IconUrl;

    private List<AppDTO> appDtos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return IconUrl;
    }

    public void setIconUrl(String iconUrl) {
        IconUrl = iconUrl;
    }

    public List<AppDTO> getAppDtos() {
        return appDtos;
    }

    public void setAppDtos(List<AppDTO> appDtos) {
        this.appDtos = appDtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
