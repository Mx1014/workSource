package com.everhomes.contentserver;

import com.everhomes.util.Name;

@Name(value = "contentserver.config.response")
public class
ConfigResponse {
    private String configName;

    private ResourceType configType;

    private int errCode;

    private String errMessage;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public ResourceType getConfigType() {
        return configType;
    }

    public void setConfigType(ResourceType configType) {
        this.configType = configType;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

}
