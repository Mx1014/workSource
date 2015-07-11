// @formatter:off
// generated at 2015-07-11 16:05:49
package com.everhomes.launchpad;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.launchpad.LaunchPadPostActionCategoryDTO;

public class FindLaunchPadPostActionCategoriesRestResponse extends RestResponseBase {

    private List<LaunchPadPostActionCategoryDTO> response;

    public FindLaunchPadPostActionCategoriesRestResponse () {
    }

    public List<LaunchPadPostActionCategoryDTO> getResponse() {
        return response;
    }

    public void setResponse(List<LaunchPadPostActionCategoryDTO> response) {
        this.response = response;
    }
}
