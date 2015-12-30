// @formatter:off
// generated at 2015-12-04 14:52:02
package com.everhomes.configuration.admin;

import com.everhomes.rest.configuration.admin.ListConfigurationsAdminCommandResponse;
import com.everhomes.rest.RestResponseBase;

public class ConfigurationRestResponse extends RestResponseBase {

    private ListConfigurationsAdminCommandResponse response;

    public ConfigurationRestResponse () {
    }

    public ListConfigurationsAdminCommandResponse getResponse() {
        return response;
    }

    public void setResponse(ListConfigurationsAdminCommandResponse response) {
        this.response = response;
    }
}
