// @formatter:off
package com.everhomes.rest.persist.server;

import javax.validation.constraints.NotNull;

public class AddPersistServerCommand {
    private Integer masterId;

    @NotNull
    private String  addressUri;
    
    private Integer addressPort;

    @NotNull
    private Integer serverType;
    
    private Integer status;
    
    private String  configTag;
    
    private String  description;

    public AddPersistServerCommand() {
    }

    public Integer getMasterId() {
        return masterId;
    }

    public void setMasterId(Integer masterId) {
        this.masterId = masterId;
    }

    public String getAddressUri() {
        return addressUri;
    }

    public void setAddressUri(String addressUri) {
        this.addressUri = addressUri;
    }

    public Integer getAddressPort() {
        return addressPort;
    }

    public void setAddressPort(Integer addressPort) {
        this.addressPort = addressPort;
    }

    public Integer getServerType() {
        return serverType;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getConfigTag() {
        return configTag;
    }

    public void setConfigTag(String configTag) {
        this.configTag = configTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
