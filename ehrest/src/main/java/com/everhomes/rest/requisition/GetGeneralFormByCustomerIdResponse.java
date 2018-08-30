package com.everhomes.rest.requisition;

import com.everhomes.util.StringHelper;

public class GetGeneralFormByCustomerIdResponse {

    private Long formOriginId;
    private Long formVersion;
    private Long sourceId;

    private Long appId;


    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

<<<<<<< HEAD
    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

=======
>>>>>>> 5.8.0
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 5.8.0
