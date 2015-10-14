// @formatter:off
// generated at 2015-10-14 12:36:35
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
