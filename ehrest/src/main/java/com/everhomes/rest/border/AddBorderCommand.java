// @formatter:off
package com.everhomes.rest.border;

import javax.validation.constraints.NotNull;

public class AddBorderCommand {
    @NotNull
    private String privateAddress;
    
    @NotNull
    private int privatePort;
    
    @NotNull
    private String publicAddress;
    
    @NotNull
    private int publicPort;
    
    @NotNull
    private Integer status;
    
    private String configTag;
    private String description;

    public AddBorderCommand() {
    }

    public String getPrivateAddress() {
        return privateAddress;
    }

    public void setPrivateAddress(String privateAddress) {
        this.privateAddress = privateAddress;
    }

    public int getPrivatePort() {
        return privatePort;
    }

    public void setPrivatePort(int privatePort) {
        this.privatePort = privatePort;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public int getPublicPort() {
        return publicPort;
    }

    public void setPublicPort(int publicPort) {
        this.publicPort = publicPort;
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
