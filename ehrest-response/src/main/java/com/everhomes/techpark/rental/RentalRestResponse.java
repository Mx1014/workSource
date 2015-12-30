// @formatter:off
// generated at 2015-12-04 14:52:03
package com.everhomes.techpark.rental;

import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.techpark.rental.UpdateRentalRuleCommandResponse;

public class RentalRestResponse extends RestResponseBase {

    private UpdateRentalRuleCommandResponse response;

    public RentalRestResponse () {
    }

    public UpdateRentalRuleCommandResponse getResponse() {
        return response;
    }

    public void setResponse(UpdateRentalRuleCommandResponse response) {
        this.response = response;
    }
}
