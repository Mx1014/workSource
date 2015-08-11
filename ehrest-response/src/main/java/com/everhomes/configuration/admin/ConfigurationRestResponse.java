// @formatter:off
// generated at 2015-08-11 15:30:30
package com.everhomes.configuration.admin;

import com.everhomes.rest.RestResponseBase;

import com.everhomes.configuration.admin.ListConfigurationsAdminCommandResponse;

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
